package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.ggdio.wsconsumer.api.constant.WSDLConstants;
import br.com.ggdio.wsconsumer.soap.model.Instance;
import br.com.ggdio.wsconsumer.soap.model.Namespace;
import br.com.ggdio.wsconsumer.soap.model.Operation;
import br.com.ggdio.wsconsumer.soap.model.Part;
import br.com.ggdio.wsconsumer.soap.model.Port;
import br.com.ggdio.wsconsumer.soap.model.Schema;
import br.com.ggdio.wsconsumer.soap.model.Service;

/**
 * SOAP Model Discovery Utility Class
 * @author Guilherme Dio
 *
 */
public final class SOAPModelDiscovery {
	
	@SuppressWarnings("unchecked")
	public static final Instance discoverModel(String wsdl, String protocol, String tns) throws WSDLException, XPathExpressionException, SAXException, IOException, ParserConfigurationException{
		//Webservice instance definition
		Instance webservice = new Instance();
		List<Service> services = new ArrayList<>();
		
		//Prepare parameters values
		webservice.setWSDL(wsdl);
		webservice.setSOAPProtocol(protocol);
		webservice.setTargetNamespace(tns);
		webservice.setServices(services);
		webservice.setElementFormDefault("qualified");
		webservice.setStyle("literal");
		
		//Prepare the reader
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        
        //Get definition and document
		Definition def = reader.readWSDL(null, wsdl);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wsdl);
		
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
					
					//Scan operation
					Operation operation = new Operation();
					port.getOperations().add(operation);
					javax.wsdl.Operation operationDef = (javax.wsdl.Operation) bindingOperation.getOperation();
					
					//Prepare values
					operation.setName(operationDef.getName());
					operation.setInput(preparePart(tns, document, operationDef.getInput().getMessage().getQName().getLocalPart(), operationDef.getInput().getMessage().getParts()));
					operation.setOutput(preparePart(tns, document, operationDef.getOutput().getMessage().getQName().getLocalPart(), operationDef.getOutput().getMessage().getParts()));
				}
			}
		}
		
		return webservice;
		
//		Service service = def.getService(new QName(tns, serviceName));
//		Port port = service.getPort(portName);
//		Operation operation = port.getBinding().getBindingOperation(operationName, inputName, outputName).getOperation();
//		
//		//Prepare output
//		Map<String, Part> outParts = operation.getOutput().getMessage().getParts();
//		for(String key : outParts.keySet()){
//			//Recover part
//			Part part = outParts.get(key);
//			
//			//Check name
//			String name = part.getElementName() != null ? part.getElementName().getLocalPart() : null;
//			if(name == null || "".equals(name))
//				name = part.getName();
//			
//			//Check type
//			Object type = part.getTypeName() != null ? part.getTypeName().getLocalPart() : null;
//			if(type == null || "".equals(type)){
//				TO xsdModel = resolveXSDModel(document, name);
//				type = xsdModel != null ? xsdModel : XSDType.STRING;
//			}
//			else{
//				if(XSDType.exists(String.valueOf(type)))
//					type = XSDType.getXSDType(String.valueOf(type));
//				else
//					type = XSDType.STRING;
//			}
//			
//			//Save
//			output.addData(name, type);
//		}
//		
//		//Set configuration
//		configuration.addData(Constants.KEY_WSDL, wsdl);
//		configuration.addData(Constants.KEY_SOAP_PROTOCOL, protocol);
//		configuration.addData(Constants.KEY_TARGET_NAMESPACE, tns);
//		configuration.addData(Constants.KEY_SERVICE, serviceName);
//		configuration.addData(Constants.KEY_PORT, portName);
//		configuration.addData(Constants.KEY_OPERATION, operationName);
//		configuration.addData(Constants.KEY_OPERATION_INPUT_NAME, inputName);
//		configuration.addData(Constants.KEY_OPERATION_INPUT_NAME, outputName);
//		configuration.addData(Constants.KEY_SOAP_PROTOCOL, protocol);
//		
//		//Set model
//		model.setInputSchema(input);
//		model.setOutputSchema(output);
//		model.setConfiguration(configuration);
//		
//		return model;
	}
	
	/**
	 * Scan part
	 * @param tns 
	 * @param document
	 * @param parts
	 * @return
	 * @throws XPathExpressionException
	 */
	private static final Part preparePart(String tns, Document document, String partName, Map<String, javax.wsdl.Part> parts) throws XPathExpressionException{
		//Part definition
		Part part = new Part();
		part.setName(partName);
		
		//Scan message schema
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
			
			//Use targetNamespace if part hasnt it
			if(nsUri == null && nsPrefix == null && tns != null){
				nsPrefix = WSDLConstants.TNS_PREFIX;
				nsUri = tns;
			}
			
			//Set namespace prefix and uri
			Namespace namespace = new Namespace(nsPrefix, nsUri);
			
			//Check type
			Schema schema = null;
			
			String typeName = partDef.getTypeName() != null ? partDef.getTypeName().getLocalPart() : null;
			if(typeName == null || "".equals(typeName)){
				schema = resolveXSDModel(namespace, document, name);
//				if(name.equals(operation.getName())){
//					input = xsdModel;
//					break;
//				}
				//Complex type(default is STRING)
//				type = schema != null ? XSDType.COMPLEX : XSDType.STRING;
			}
			else{
				XSDType type = null;
				//Define schema type(default is STRING)
				if(XSDType.exists(String.valueOf(typeName)))
					type = XSDType.getXSDType(String.valueOf(typeName));
				else
					type = XSDType.STRING;
				
				//Define schema
				schema = new Schema(name, namespace, type, null, null);
				schema.setType(type);
			}
			
			//Save
			part.putParameterSchema(schema.getName(), schema);
		}
		return part;
	}
	
	private static final Schema resolveXSDModel(Namespace partNamespace, Object scope, String typeName) throws XPathExpressionException{
		//Prepare XPATH
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		//Queries
		List<String> queries = Arrays.asList("//definitions/types/schema/simpleType[@name='" + typeName + "']/sequence/element",
											 "//definitions/types/schema/complexType[@name='" + typeName + "']/sequence/element",
											 "//definitions/types/schema/element[@name='" + typeName + "']/simpleType/sequence/element",
											 "//definitions/types/schema/element[@name='" + typeName + "']/complexType/sequence/element",
											 "//definitions/types/schema/element[@name='" + typeName + "']");
		//Search
		NodeList elements = null;
		for(String query : queries){
			elements = (NodeList) xpath.compile(query).evaluate(scope, XPathConstants.NODESET);
			if(elements.getLength() != 0)
				break;
		}
		
		Schema root = null;
		Schema previous = null;
		//Iterate over the elements and fill the model
		for(byte c=0;c<elements.getLength();c++){
			Schema model = new Schema();
			Node item = elements.item(c);
			
			//Define root schema
			if(previous != null)
				previous.setNext(model);
			else
				root = model;
			
			previous = model;
			NamedNodeMap attributes = elements.item(c).getAttributes();
			String name = attributes.getNamedItem("name").getTextContent();
			String type = attributes.getNamedItem("type") != null ? removeNSAlias(attributes.getNamedItem("type").getTextContent()) : null;
			String nsPrefix = item.getPrefix();
			String nsUri = item.getNamespaceURI();
			
			//Resolve namespace to part one
			if(nsUri == null && nsPrefix == null && partNamespace != null){
				nsPrefix = partNamespace.getPrefix();
				nsUri = partNamespace.getURI();
			}
			
			model.setNamespace(new Namespace(nsPrefix, nsUri));
			model.setName(name);
			//If exists, then its not composed
			if(XSDType.exists(type)){
				model.setType(XSDType.getXSDType(type));
			}
			//If not, the search for the specific type
			else{
				Schema innerType = resolveXSDModel(partNamespace, scope, type);
				if(innerType != null){
					//Avoid root element duplicity
					if(name.equals(typeName))
						return innerType;
				}
				model.setType(XSDType.COMPLEX);
				model.setInner(innerType);
			}
		}
		
		return root;
	}
	
//	private static final Schema newResolveXSDModel(Object scope, String typeName) throws XPathExpressionException{
//		//Object to return
//		Schema model = new Schema();
//		
//		//Prepare XPATH
//		XPathFactory factory = XPathFactory.newInstance();
//		XPath xpath = factory.newXPath();
//		
//		//Search for all node types inside schema node with name equals to 'typeName'
//		String simpleTypeQuery = "//definitions/types/schema/*[@name='" + typeName + "']";
//		NodeList elements = (NodeList) xpath.compile(simpleTypeQuery).evaluate(scope, XPathConstants.NODESET);
//		
//		//Iterate over the elements and fill the model
//		for(byte c=0;c<elements.getLength();c++){
//			//Recover node data
//			Node node = elements.item(c);
//			NamedNodeMap attributes = node.getAttributes();
//			
//			//Handle types specifically	
//			String nodeName = removeNSAlias(node.getNodeName()).toUpperCase();
//			switch (nodeName) {
//				case WSDLConstants.ELEMENT:
//					handleElement(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.SIMPLETYPE:
//					handleSimpleType(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.COMPLEXTYPE:
//					handleComplexType(scope, elements, xpath, node);		
//					break;
//				case WSDLConstants.SEQUENCE:
//					handleSequence(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.COMPLEXCONTENT:
//					handleComplexContent(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.EXTENSION:
//					handleExtension(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.RESTRICTION:
//					handleRestriction(scope, elements, xpath, node);
//					break;
//				case WSDLConstants.ENUMERATION:
//					handleEnumeration(scope, elements, xpath, node);
//					break;
//				default:
//					handleDefault(scope, elements, xpath, node);
//					break;
//			}
//			
//			String name = attributes.getNamedItem("name").getTextContent();
//			String type = attributes.getNamedItem("type") != null ? removeNSAlias(attributes.getNamedItem("type").getTextContent()) : name;
//			String nsPrefix = node.getPrefix();
//			String nsUri = node.getNamespaceURI();
//			
//			model.setName(name);
//			model.setNamespace(new Namespace(nsPrefix, nsUri));
//		}
//		
//		return model;
//	}
//	
//	private static Schema handleDefault(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleEnumeration(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleRestriction(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleExtension(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleComplexContent(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleSequence(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleComplexType(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleSimpleType(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		return null;
//	}
//
//	private static Schema handleElement(Object scope, NodeList nodeList, XPath xpath, Node node) throws XPathExpressionException {
//		NamedNodeMap attributes = node.getAttributes();
//		Namespace namespace = new Namespace(node.getPrefix(), node.getNamespaceURI());
//		String name = attributes.getNamedItem("name").getTextContent();
//		Schema schema = new Schema(name, namespace, null, null);
//		
//		//Handle element
//		if(attributes.getNamedItem("type") == null){
//			//No type, so it has childrens
//			Schema innerSchema = resolveXSDModel(node.getChildNodes(), name);
//			
//			//Check inner schema
//			if(innerSchema == null){
//				schema.setType(XSDType.STRING);
//			}
//			else{
//				schema.setType(XSDType.COMPLEX);
//				schema.setInner(innerSchema);
//			}
//		}
//		else{
//			String strType = attributes.getNamedItem("type").getTextContent();
//			if(XSDType.exists(strType))
//				schema.setType(XSDType.getXSDType(strType));
//			//If not, the search for the specific type
//			else{
//				Schema innerType = newResolveXSDModel(scope, strType);
////				if(innerType != null){
//					//Avoid root element duplicity
////					if(name.equals(typeName))
////						return innerType;
////				}
//				schema.setType(XSDType.COMPLEX);
//				schema.setInner(innerType);
//			}
//		}
//		return schema;
//	}

	private static final String removeNSAlias(String value){
		value = String.valueOf(value);
		return value.substring(value.indexOf(":") + 1);
	}
	
//	private static final Schema getSchema(Definition definition){
//	for(Object elm : definition.getTypes().getExtensibilityElements())
//		if(elm instanceof Schema)
//			return (Schema) elm;
//	return null;
//}
	
}