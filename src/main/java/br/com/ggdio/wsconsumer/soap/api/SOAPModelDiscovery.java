package br.com.ggdio.wsconsumer.soap.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Body;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.ggdio.wsconsumer.soap.api.constant.WSDLConstants;
import br.com.ggdio.wsconsumer.soap.model.Instance;
import br.com.ggdio.wsconsumer.soap.model.Namespace;
import br.com.ggdio.wsconsumer.soap.model.Operation;
import br.com.ggdio.wsconsumer.soap.model.Part;
import br.com.ggdio.wsconsumer.soap.model.Port;
import br.com.ggdio.wsconsumer.soap.model.Schema;
import br.com.ggdio.wsconsumer.soap.model.Service;
import br.com.ggdio.wsconsumer.soap.model.XSDType;
import br.com.ggdio.wsconsumer.soap.util.SOAPUtil;

/**
 * SOAP Model Discovery Utility Class
 * @author Guilherme Dio
 *
 */
public class SOAPModelDiscovery {
	
	/**
	 * Discover WSDL structure
	 * @param wsdl - The wsdl URI
	 * @param protocol - The SOAP Protocol
	 * @param elementFormDefault - qualified/unqualified
	 * @param style - The SOAP style
	 * @return {@link Instance}
	 * @throws WSDLException
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	@SuppressWarnings("unchecked")
	public Instance discoverModel(String wsdl) throws WSDLException, XPathExpressionException, SAXException, IOException, ParserConfigurationException{
		//Webservice instance definition
		Instance webservice = new Instance();
		List<Service> services = new ArrayList<>();
		
		//Prepare parameters values
		webservice.setWSDL(wsdl);
		webservice.setServices(services);
		
		//Should define here the element form default(qualified|unqualified)
		
		//Prepare the reader
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        
        //Get definition and document
		Definition def = reader.readWSDL(null, wsdl);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wsdl);

		//Set the target namespace
		String tns = def.getTargetNamespace();
		webservice.setTargetNamespace(tns);
		
		//Scan services
		Set<QName> serviceKeys = def.getServices().keySet();
		for(QName serviceKey : serviceKeys){
			
			//Scan service
			Service service = new Service();
			services.add(service);
			javax.wsdl.Service serviceDef = (javax.wsdl.Service) def.getServices().get(serviceKey);
			
			//Prepare values
			service.setName(serviceDef.getQName().getLocalPart());
			service.setNamespace(new Namespace(serviceDef.getQName().getPrefix(), serviceDef.getQName().getNamespaceURI()));
			service.setPorts(new ArrayList<Port>());
			
			//Scan ports
			Set<String> portKeys = serviceDef.getPorts().keySet();
			for(String portKey : portKeys){
				
				//Scan port
				Port port = new Port();
				service.getPorts().add(port);
				javax.wsdl.Port portDef = serviceDef.getPort(portKey);
				
				//Prepare values
				port.setName(portDef.getName());
				port.setOperations(new ArrayList<Operation>());
				
				//Scan operations
				List<BindingOperation> bindingOperations = portDef.getBinding().getBindingOperations();
				for(BindingOperation bindingOperation : bindingOperations){
					Operation operation = new Operation();
					
					//Get soapAction
					operation.setSOAPAction(getSOAPAction(bindingOperation));
				
					//Scan operation
					port.getOperations().add(operation);
					javax.wsdl.Operation operationDef = (javax.wsdl.Operation) bindingOperation.getOperation();
					
					//Prepare values
					operation.setName(operationDef.getName());
					operation.setInput(preparePart(tns, document, operationDef.getInput().getMessage().getQName().getLocalPart(), operationDef.getInput().getMessage().getParts()));
					operation.setOutput(preparePart(tns, document, operationDef.getOutput().getMessage().getQName().getLocalPart(), operationDef.getOutput().getMessage().getParts()));
					
					//Code responsible for discovering the soap protocol for the target operation
					Object body = bindingOperation.getBindingInput().getExtensibilityElements().get(0);
					if(body instanceof SOAP12Body){
						operation.setSOAPProtocol(SOAPConstants.SOAP_1_2_PROTOCOL);
						operation.setUse(((SOAP12Body) body).getUse());
					}
					else{
						operation.setSOAPProtocol(SOAPConstants.SOAP_1_1_PROTOCOL);
						operation.setUse(((SOAPBody) body).getUse());
					}
				}
			}
		}
		return webservice;
	}

	/**
	 * Read the soap action from binding operation
	 * @param bindingOperation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getSOAPAction(BindingOperation bindingOperation) {
		List<Object> extElms = bindingOperation.getExtensibilityElements();
		if(extElms != null && extElms.size() > 0)
			for(Object elm : extElms)
				if(elm instanceof SOAPOperation)
					return ((SOAPOperation) elm).getSoapActionURI();
		return "";
	}
	
	/**
	 * Scan part
	 * @param tns - The target namespace
	 * @param document - The XML document
	 * @param parts - The WSDL Operation parts
	 * @return {@link javax.wsdl.Part}
	 * @throws XPathExpressionException
	 */
	private Part preparePart(String tns, Document document, String partName, Map<String, javax.wsdl.Part> parts) throws XPathExpressionException{
		//Part definition
		Part part = new Part();
		if(parts.size() > 1)
			part.setName(partName);
		
		//Scan message schema
		Schema previous = null;
		for(String key : parts.keySet()){
			
			//Recover part
			javax.wsdl.Part partDef = parts.get(key);
			
			//Check attributes
			String name = "";
			String nsPrefix = null;
			String nsUri = null;
			if(partDef.getElementName() != null){
				name = partDef.getElementName().getLocalPart();
				nsPrefix = partDef.getElementName().getPrefix();
				nsUri = partDef.getElementName().getNamespaceURI();
			}
			else
				name = partDef.getName();
			
			//Set name for single part case, when its defined on schema section of the wsdl
			if(part.getName().equals(""))
				part.setName(name);
			
			//Use targetNamespace if part hasnt it
			if(nsUri == null && nsPrefix == null && tns != null){
				nsPrefix = WSDLConstants.TNS_PREFIX;
				nsUri = tns;
			}
			
			//Set namespace prefix and uri
			Namespace namespace = new Namespace(nsPrefix, nsUri);
			
			//Check type
			Schema schema = null;
			
			//Identify type and scan the schema model
			String typeName = partDef.getTypeName() != null ? partDef.getTypeName().getLocalPart() : null;
			if(typeName == null || "".equals(typeName))
				schema = resolveXSDModel(namespace, document, name);
			else{
				XSDType type = null;
				//Define schema type(default is STRING)
				if(XSDType.exists(String.valueOf(typeName)))
					type = XSDType.getXSDType(String.valueOf(typeName));
				else
					type = XSDType.STRING;
				
				//Define schema
				schema = new Schema(name, namespace, type, null, null, null, null, WSDLConstants.ELEMENT_FORM_DEFAULT_QUALIFIED);
				schema.setType(type);
			}
			
			//Prepare next
			if(previous == null)
				previous = schema;
			else{
				previous.setNext(schema);
				previous = schema;
			}
			
			//Save root
			if(part.getRootSchema() == null)
				part.setRootSchema(schema);
		}
		return part;
	}
	
	/**
	 * Resolve the XSD Model
	 * @param partNamespace - The parent part namespace
	 * @param scope - The scope(nodeList)
	 * @param typeName - The type to be scanned
	 * @return {@link Schema}
	 * @throws XPathExpressionException - If something wrong occurs while searching for the element
	 */
	private Schema resolveXSDModel(Namespace partNamespace, Object scope, String typeName) throws XPathExpressionException{
		//Prepare XPATH
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		//XSD Elements
		NodeList elements = null;
		String xsdNSURI = "";
		String elementFormDefault = WSDLConstants.ELEMENT_FORM_DEFAULT_QUALIFIED;
		
		//Iterate over XSD
		NodeList xsdSchemas = (NodeList) xpath.compile("//definitions/types/schema").evaluate(scope, XPathConstants.NODESET);
		for(byte c=0;c<xsdSchemas.getLength();c++){
			//Get XSD
			Node xsdSchema = xsdSchemas.item(c);
			NodeList innerScope = xsdSchema.getChildNodes();
			
			//Flag for elements found
			Boolean found = false;

			//Search
			for(String query : getXPathQueries(typeName)){
				elements = (NodeList) xpath.compile(query).evaluate(innerScope, XPathConstants.NODESET);
				found = elements.getLength() > 0;
				if(found)
					break;
			}
			
			//Check if the elemets has been found
			if(found){
				//Prepare the targetNamespace
				NamedNodeMap xsdAttrs = xsdSchema.getAttributes();
				Node tnsAttr = xsdAttrs.getNamedItem(WSDLConstants.TARGET_NAMESPACE);
				Node efdAttr = xsdAttrs.getNamedItem(WSDLConstants.ELEMENT_FORM_DEFAULT);
				if(tnsAttr != null)
					xsdNSURI = tnsAttr.getTextContent();
				if(efdAttr != null)
					elementFormDefault = efdAttr.getTextContent();
				break;
			}
		}
		
		//Didnt find any child
		if(elements == null || elements.getLength() == 0)
			return null;
		
		//Strategy per node type
		String et = SOAPUtil.removeNSAlias(elements.item(0).getNodeName());
		switch (et.toUpperCase()) {
			//ENUMERATION type
			case WSDLConstants.ENUMERATION:
				return handleEnumeration(elements, xsdNSURI, elementFormDefault, partNamespace, scope, typeName);
				
			//ELEMENT as default type
			case WSDLConstants.ELEMENT:
				return handleElement(elements, xsdNSURI, elementFormDefault, partNamespace, scope, typeName);
			
			default:
				return null;
		}
		
	}

	/**
	 * Handle enumeration type
	 * @param elements
	 * @param xsdNSURI
	 * @param partNamespace
	 * @param scope
	 * @param typeName
	 * @return
	 */
	private Schema handleEnumeration(NodeList elements, String xsdNSURI, String efd, Namespace partNamespace, Object scope, String typeName) {
		Schema schema = new Schema(typeName, new Namespace("", ""), XSDType.ENUMERATION, null, null, null, null, efd);
		Schema previous = null;
		//Iterate over the elements and fill the model
		for(byte c=0;c<elements.getLength();c++){
			Node item = elements.item(c);
			NamedNodeMap attributes = item.getAttributes();
			String value = attributes.getNamedItem("value").getTextContent();
			Schema model = new Schema(value.toUpperCase(), new Namespace("", ""), XSDType.STRING, null,null, null, null, efd);
			if(schema.getInner() == null)
				schema.setInner(model);
			else{
				if(previous == null)
					previous = model;
				else
					previous.setNext(model);
			}
		}
		return schema;
	}

	/**
	 * Handle element type
	 * @param elements
	 * @param xsdNSURI
	 * @param partNamespace
	 * @param scope
	 * @param typeName
	 * @return
	 * @throws XPathExpressionException
	 */
	private Schema handleElement(NodeList elements, String xsdNSURI, String efd, Namespace partNamespace, Object scope, String typeName) throws XPathExpressionException {
		if(elements.getLength() == 0) return null;
		
		//Iterate over the elements and fill the model
		Schema root = null;
		Schema previous = null;
		for(byte c=0;c<elements.getLength();c++){
			Schema model = new Schema();
			Node item = elements.item(c);
			
			//Define root schema
			if(previous != null){
				previous.setNext(model);
				model.setPrevious(previous);
			}
			else
				root = model;
			
			//Define previous schema as the current.
			previous = model;
			
			//Resolve attributes
			NamedNodeMap attributes = item.getAttributes();
			String name = attributes.getNamedItem("name").getTextContent();
			String type = attributes.getNamedItem("type") != null ? SOAPUtil.removeNSAlias(attributes.getNamedItem("type").getTextContent()) : null;
			String maxOccurs = attributes.getNamedItem("maxOccurs") != null ? attributes.getNamedItem("maxOccurs").getTextContent() : "1";
			
			//Prepare namespace
			String nsPrefix = item.getPrefix();
			String nsUri = item.getNamespaceURI();
			if(nsUri == null && nsPrefix == null)
				nsUri = xsdNSURI;
			
			//Set model initial values
			model.setName(name);
			model.setNamespace(new Namespace(nsPrefix, nsUri));
			model.setElementFormDefault(efd);
			
			//Check if type exists
			if(XSDType.exists(type)){
				//If exists type, then its not composed
				model.setType(XSDType.getXSDType(type));
			}
			else{
				//If not, then search for the specific type
				Schema innerType = resolveXSDModel(partNamespace, scope, type);
				if(innerType != null){
					//Avoid root element duplicity
					if(name.equals(typeName))
						return innerType;
				}
				model.setType(XSDType.COMPLEX);
				model.setInner(innerType);
				innerType.setUpper(model);
				
				//Check if its a List(if so, then wrap it inside another schema)
				if(maxOccurs.equals("unbounded"))
					return new Schema(typeName, new Namespace(nsPrefix, nsUri), XSDType.LIST, null, model, null, null, efd);
			}
		}
		
		//Handle extensions
		Node checkExtension = elements.item(0).getParentNode().getParentNode();
		if(checkExtension != null && SOAPUtil.removeNSAlias(checkExtension.getNodeName()).equalsIgnoreCase(WSDLConstants.EXTENSION)){
			//Get base name and search it
			String base = SOAPUtil.removeNSAlias(checkExtension.getAttributes().getNamedItem("base").getTextContent());
			Schema extension = resolveXSDModel(partNamespace, scope, base);
			
			//Plug the extension after the last schema
			previous.setNext(extension);
		}
		
		//Return the root schema
		return root;
	}
	
	/**
	 * Return the possible xpath queries for elements structure
	 * @param typeName
	 * @return List of xpath queries
	 */
	private List<String> getXPathQueries(String typeName) {
		return Arrays.asList("simpleType[@name='" + typeName + "']/sequence/element",
							 "complexType[@name='" + typeName + "']/sequence/element",
							 "element[@name='" + typeName + "']/simpleType/sequence/element",
							 "element[@name='" + typeName + "']/complexType/sequence/element",
							 "simpleType[@name='" + typeName + "']/restriction/enumeration",
							 "complexType[@name='" + typeName + "']/complexContent/extension/sequence/element",
							 "element[@name='" + typeName + "']");
	}
	
}