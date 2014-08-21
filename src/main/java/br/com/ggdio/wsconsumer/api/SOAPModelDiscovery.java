package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.ggdio.wsconsumer.soap.model.Part;

/**
 * SOAP Model Discovery Utility Class
 * @author Guilherme Dio
 *
 */
public final class SOAPModelDiscovery {
	
	public static final Part discoverModel(String wsdl, String protocol, String tns, String serviceName, String portName, String operationName, String inputName, String outputName) throws WSDLException, XPathExpressionException, SAXException, IOException, ParserConfigurationException{
		final Part model = new Part();
		final TO input = new TO();
		final TO output = new TO();
		final TO configuration = new TO();
		
		//Prepare the reader
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        
        //Stablish the soap definition
		Definition def = reader.readWSDL(null, wsdl);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wsdl);
		Service service = def.getService(new QName(tns, serviceName));
		Port port = service.getPort(portName);
		Operation operation = port.getBinding().getBindingOperation(operationName, inputName, outputName).getOperation();
		
		//Prepare input
		@SuppressWarnings("unchecked")
		Map<String, Part> inParts = operation.getInput().getMessage().getParts();
		for(String key : inParts.keySet()){
			//Recover part
			Part part = inParts.get(key);
			
			//Check name
			String name = part.getElementName() != null ? part.getElementName().getLocalPart() : null;
			if(name == null || "".equals(name))
				name = part.getName();
			
			//Check type
			Object type = part.getTypeName() != null ? part.getTypeName().getLocalPart() : null;
			if(type == null || "".equals(type)){
				TO xsdModel = resolveXSDModel(document, name);
//				if(name.equals(operation.getName())){
//					input = xsdModel;
//					break;
//				}
				type = xsdModel != null ? xsdModel : XSDType.STRING;
			}
			else{
				if(XSDType.exists(String.valueOf(type)))
					type = XSDType.getXSDType(String.valueOf(type));
				else
					type = XSDType.STRING;
			}
				
			//Save
			input.addData(name, type);
		}
		
		//Prepare output
		@SuppressWarnings("unchecked")
		Map<String, Part> outParts = operation.getOutput().getMessage().getParts();
		for(String key : outParts.keySet()){
			//Recover part
			Part part = outParts.get(key);
			
			//Check name
			String name = part.getElementName() != null ? part.getElementName().getLocalPart() : null;
			if(name == null || "".equals(name))
				name = part.getName();
			
			//Check type
			Object type = part.getTypeName() != null ? part.getTypeName().getLocalPart() : null;
			if(type == null || "".equals(type)){
				TO xsdModel = resolveXSDModel(document, name);
				type = xsdModel != null ? xsdModel : XSDType.STRING;
			}
			else{
				if(XSDType.exists(String.valueOf(type)))
					type = XSDType.getXSDType(String.valueOf(type));
				else
					type = XSDType.STRING;
			}
			
			//Save
			output.addData(name, type);
		}
		
		//Set configuration
		configuration.addData(Constants.KEY_WSDL, wsdl);
		configuration.addData(Constants.KEY_SOAP_PROTOCOL, protocol);
		configuration.addData(Constants.KEY_TARGET_NAMESPACE, tns);
		configuration.addData(Constants.KEY_SERVICE, serviceName);
		configuration.addData(Constants.KEY_PORT, portName);
		configuration.addData(Constants.KEY_OPERATION, operationName);
		configuration.addData(Constants.KEY_OPERATION_INPUT_NAME, inputName);
		configuration.addData(Constants.KEY_OPERATION_INPUT_NAME, outputName);
		configuration.addData(Constants.KEY_SOAP_PROTOCOL, protocol);
		
		//Set model
		model.setInputSchema(input);
		model.setOutputSchema(output);
		model.setConfiguration(configuration);
		
		return model;
	}
	
	private static final TO resolveXSDModel(Object scope, String typeName) throws XPathExpressionException{
		//Object to return
		TO model = new TO();
		
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
			
			//If exists, then its not composed
			if(XSDType.exists(type))
				model.addData(name, XSDType.getXSDType(type));
			//If not, the search for the specific type
			else{
				Object innerType = resolveXSDModel(scope, type);
				if(innerType != null){
					//Avoid root element duplicity
					if(name.equals(typeName))
						return (TO) innerType;
				}
				else
					innerType = XSDType.STRING;
				model.addData(name,innerType);
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