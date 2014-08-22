package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	public static final Instance discoverModel(String wsdl, String protocol, String tns, String serviceName, String portName, String operationName, String inputName, String outputName) throws WSDLException, XPathExpressionException, SAXException, IOException, ParserConfigurationException{
		List<Service> services = new ArrayList<>();
		Instance webservice = new Instance();
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
		
		//Scan webservice features
		Set<String> serviceKeys = def.getServices().keySet();
		for(String serviceKey : serviceKeys){
			Service service = new Service();
			javax.wsdl.Service serviceDef = (javax.wsdl.Service) def.getServices().get(serviceKey);
			service.setName(serviceDef.getQName().getLocalPart());
			service.setNamespace(new Namespace(serviceDef.getQName().getPrefix(), serviceDef.getQName().getNamespaceURI()));
			service.setPorts(new ArrayList<Port>());
			Set<String> portKeys = serviceDef.getPorts().keySet();
			for(String portKey : portKeys){
				Port port = new Port();
				service.getPorts().add(port);
				javax.wsdl.Port portDef = serviceDef.getPort(portKey);
				port.setName(portDef.getName());
				port.setOperations(new ArrayList<Operation>());
				List<BindingOperation> bindingOperations = portDef.getBinding().getBindingOperations();
				for(BindingOperation bindingOperation : bindingOperations){
					Operation operation = new Operation();
					javax.wsdl.Operation operationDef = (javax.wsdl.Operation) bindingOperation;
					
					//Prepare Input
					operation.setInput(preparePart(document, bindingOperation.getOperation().getInput().getMessage().getParts()));
					
					//Prepare Output
					operation.setOutput(preparePart(document, bindingOperation.getOperation().getOutput().getMessage().getParts()));
					
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
	
	private static final Part preparePart(Document document, Map<String, javax.wsdl.Part> parts) throws XPathExpressionException{
		Part part = new Part();
		part.setParametersSchema(new ArrayList<Schema>());
		
		//Prepare input
		for(String key : parts.keySet()){
			//Recover part
			javax.wsdl.Part partDef = parts.get(key);
			
			//Check name
			String name = partDef.getElementName() != null ? partDef.getElementName().getLocalPart() : null;
			if(name == null || "".equals(name))
				name = partDef.getName();
			
			String prefix = partDef.getElementName().getPrefix();
			String nsUri = partDef.getElementName().getNamespaceURI();
			Namespace namespace = new Namespace(prefix, nsUri);
			
			//Check type
			Schema schema = null;
			XSDType type = null;
			String typeName = partDef.getTypeName() != null ? partDef.getTypeName().getLocalPart() : null;
			if(typeName == null || "".equals(typeName)){
				schema = resolveXSDModel(document, name);
//				if(name.equals(operation.getName())){
//					input = xsdModel;
//					break;
//				}
				type = schema != null ? XSDType.COMPLEX : XSDType.STRING;
			}
			else{
				if(XSDType.exists(String.valueOf(typeName)))
					type = XSDType.getXSDType(String.valueOf(typeName));
				else
					type = XSDType.STRING;
			}
			
			//Save
			part.setName(name);
			part.getParametersSchema().add(schema);
		}
		
		return part;
	}
	
	private static final Schema resolveXSDModel(Object scope, String typeName) throws XPathExpressionException{
		//Object to return
		Schema model = new Schema();
		
		//Prepare XPATH
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		//Queries
		String simpleTypeQuery = "//definitions/types/schema/simpleType[@name='" + typeName + "']/sequence/element";
		String complexTypeQuery = "//definitions/types/schema/complexType[@name='" + typeName + "']/sequence/element";
		String elementQuery = "//definitions/types/schema/element[@name='" + typeName + "']";
		
		//Search for 'element' nodes
		NodeList elements = (NodeList) xpath.compile(simpleTypeQuery).evaluate(scope, XPathConstants.NODESET);
		
		//If not found, search 'simpleType' nodes
		if(elements.getLength() == 0){
			elements = (NodeList) xpath.compile(complexTypeQuery).evaluate(scope, XPathConstants.NODESET);
		
			//If not found, search for 'complexType' nodes
			if(elements.getLength() == 0){
				elements = (NodeList) xpath.compile(elementQuery).evaluate(scope, XPathConstants.NODESET);
				
				//If cant find anything, then return null(upper level should treat it)
				if(elements.getLength() == 0)
					return null;
			}
		}
		
		//Iterate over the elements and fill the model
		for(byte c=0;c<elements.getLength();c++){
			NamedNodeMap attributes = elements.item(c).getAttributes();
			String name = attributes.getNamedItem("name").getTextContent();
			String type = removeNSAlias(attributes.getNamedItem("type").getTextContent());
			String nsPrefix = elements.item(c).getPrefix();
			String nsUri = elements.item(c).getNamespaceURI();
			
			model.setNamespace(new Namespace(nsPrefix, nsUri));
			model.setName(name);
			//If exists, then its not composed
			if(XSDType.exists(type))
				model.setType(XSDType.getXSDType(type));
			//If not, the search for the specific type
			else{
				Schema innerType = resolveXSDModel(scope, type);
				if(innerType != null){
					//Avoid root element duplicity
					if(name.equals(typeName))
						return innerType;
				}
				model.setType(XSDType.COMPLEX);
				model.setInner(innerType);
			}
		}
		
		return model;
	}
	
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