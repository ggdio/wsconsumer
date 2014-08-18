package br.com.ggdio.wsconsumer.api;

import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class SOAPModelDiscovery {
	
	public static final TO discoverModel(String wsdl, String tns, String serviceName, String portName, String operationName, String inputName, String outputName) throws WSDLException{
		final TO model = new TO();
		final TO input = new TO();
		final TO output = new TO();
		
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", true);
        reader.setFeature("javax.wsdl.importDocuments", true);
		Definition def = reader.readWSDL(null, wsdl);
		Schema schema = getSchema(def);
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
			String type = part.getTypeName() != null ? part.getTypeName().getLocalPart() : null;
			if(type == null || "".equals(type))
				type = Types.STRING.value();
			
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
			String type = part.getTypeName() != null ? part.getTypeName().getLocalPart() : null;
			if(type == null || "".equals(type))
				type = Types.STRING.value();
			
			//Save
			output.addData(name, type);
		}
		
		//Set model
		model.addData(Constants.KEY_INPUT_PARTS, input);
		model.addData(Constants.KEY_OUTPUT_PARTS, output);
		return model;
	}
	
	private static final Schema getSchema(Definition definition){
		for(Object elm : definition.getTypes().getExtensibilityElements())
			if(elm instanceof Schema)
				return (Schema) elm;
		return null;
	}
	
	private static final void getComplexType(Schema schema, String typeName){
		Element element = schema.getElement();
		if(element.hasChildNodes()){
			NodeList nodes = element.getChildNodes();
		}
	}
	
	private static final void findComplexTypeNode(NodeList nodeList, String typeName){
		for(byte c = 0;c < nodeList.getLength();c++){
			Node node = nodeList.item(c);
			
		}
	}
	
}
//getConsumer().getWsdlDefinition().getService(new javax.xml.namespace.QName("http://tempuri.org/","CalcPrecoPrazoWS")).getPort("CalcPrecoPrazoWSSoap").getBinding().getBindingOperation("CalcPrazo", null, null)