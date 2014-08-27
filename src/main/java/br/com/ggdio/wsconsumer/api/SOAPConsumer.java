package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.ggdio.wsconsumer.soap.invoke.Invocation;
import br.com.ggdio.wsconsumer.soap.invoke.SchemaValue;
import br.com.ggdio.wsconsumer.soap.model.Instance;
import br.com.ggdio.wsconsumer.soap.model.Namespace;
import br.com.ggdio.wsconsumer.soap.model.Part;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * SOAP Consumer utility
 * @author Guilherme Dio
 *
 */
public class SOAPConsumer {
	
	private static final String NAMESPACE_PREFIX = "tns";
	
	private final Definition wsdlDefinition;
	private final Instance webservice;
	
	public SOAPConsumer(Instance webservice) throws WSDLException {
		//Handle npe
		if(webservice == null)
			throw new NullPointerException("Webservice instance detail bean must not be null");
		
		//Fields
		this.wsdlDefinition = WSDLFactory.newInstance().newWSDLReader().readWSDL(null, webservice.getWSDL());
		this.webservice = webservice;
	}
	
	public SchemaValue invoke(Invocation invocation) throws SOAPException, IOException, XPathExpressionException {
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		try{
			SOAPMessage response = SOAPConnectionFactory.newInstance().createConnection().call(compileRequest(invocation), getWebservice().getWSDL());
			System.out.print("Response SOAP Message = ");
			response.writeTo(System.out);
			System.out.println();
			return parseResponse(response, invocation.getOperation().getOutput());
		}
		finally{
			connection.close();
		}
    }
	
	public SOAPMessage invoke(Dispatch<SOAPMessage> dispatcher, Invocation invocation) throws SOAPException, IOException {
		return dispatcher.invoke(compileRequest(invocation));
    }
	
	private SchemaValue parseResponse(SOAPMessage response, Part structure) throws SOAPException, XPathExpressionException {
		return parseResponse(response.getSOAPBody().getChildNodes(), structure);
	}
	
	private SchemaValue parseResponse(NodeList scope, Part structure) throws XPathExpressionException {
		SchemaValue root = new SchemaValue();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		Set<String> names = structure.getParametersSchemaNames();
		for(String name : names){
			
			//Recover the schema by name
			Schema schema = structure.getParameterSchema(name);
			
			if(schema.getInner() != null){
				//NESTED FIELDS
				root.putInnerParameterValue(schema.getName(), parseResponse(xPath, scope, schema.getInner()));
			}
			else{
				//PLAIN FIELD
				resolveValue(xPath, scope, root, schema);
			}
		}
		return root;
	}
	
	private SchemaValue parseResponse(XPath xPath, NodeList scope, Schema schema) throws XPathExpressionException{
		SchemaValue nested = new SchemaValue();
		Schema next = schema;
		do{
			if(next.getInner() != null){
				//NESTED FIELDS
				nested.putInnerParameterValue(next.getName(), parseResponse(xPath, scope, next.getInner()));
			}
			else{
				//PLAIN FIELD
				resolveValue(xPath, scope, nested, next);
			}
		} while((next = next.getNext()) != null);
		return nested;
	}

	private void resolveValue(XPath xPath, NodeList scope, SchemaValue nested, Schema schema) throws XPathExpressionException {
		//Search for the plain node by schema name
		NodeList result = (NodeList) xPath.compile("//*[local-name()='" + schema.getName() + "']").evaluate(scope, XPathConstants.NODESET);
		
		//Handle blank or null
		if(result.getLength() == 0){
			nested.putParameterValue(schema.getName(), null);
			return;
		}
		
		//Retrieve the item
		Node item = result.item(0);
		
		//Recover the plain value and convert it to a native one
		String plainValue = item.getTextContent();
		Object nativeValue = schema.getType().getConverter().toObject(plainValue);
		
		//Put the parameter native value
		nested.putParameterValue(schema.getName(), nativeValue);
	}
	
//	private SchemaValue parseResponse(NodeList nodes, Part structure){
//		SchemaValue value = new SchemaValue();
//		for(byte c=0;c<nodes.getLength();c++){
//			Node item = nodes.item(c);
//			String nodeName = SOAPUtil.removeNSAlias(item.getNodeName());
//			if(!nodeName.equals(structure.getName())){
//				Schema schema = structure.getParameterSchema(nodeName);
//				Node aux = item;
//				while(schema == null){
//					if(!item.hasChildNodes()) break;
//					aux = aux.getChildNodes().item(0);
//					nodeName = SOAPUtil.removeNSAlias(item.getNodeName());
//					schema = structure.getParameterSchema(nodeName);
//					if(schema != null)
//						item = aux.getParentNode();
//				}
//				if(item.hasChildNodes() && !(item.getFirstChild() instanceof Text)){
//					//INNER SCHEMA
//					value.putInnerParameterValue(nodeName, parseResponse(item.getChildNodes(), schema));
//				}
//				else{
//					//PLAIN SCHEMA
//					String textValue = item.getTextContent();
//					Converter<?> converter = schema.getType().getConverter();
//					Object nativeValue = converter.toObject(textValue);
//					value.putParameterValue(nodeName, nativeValue);
//				}
//			}
//			else{
//				//ROOT
//				value = parseResponse(item.getChildNodes(), structure);
//			}
//				
//		}
//		return value;
//	}
//	
//	private SchemaValue parseResponse(NodeList nodes, Schema schema){
//		SchemaValue value = new SchemaValue();
//		for(byte c=0;c<nodes.getLength();c++){
//			Node item = nodes.item(c);
//			String nodeName = SOAPUtil.removeNSAlias(item.getNodeName());
//			Schema next = schema;
//			do{
//				if(nodeName.equals(next.getName())){
//					if(next.getInner() != null){
//						//INNER SCHEMA
//						value.putInnerParameterValue(nodeName, parseResponse(item.getChildNodes(), next));
//					}
//					else{
//						//PLAIN SCHEMA
//						String textValue = item.getTextContent();
//						Converter<?> converter = next.getType().getConverter();
//						Object nativeValue = converter.toObject(textValue);
//						value.putParameterValue(nodeName, nativeValue);
//					}
//				}
//			} while((next = next.getNext()) != null);
//		}
//		return value;
//	}
	
	private SOAPMessage compileRequest(Invocation invocation) throws SOAPException, IOException {
        //Create message
		SOAPMessage soapMessage = MessageFactory.newInstance(getWebservice().getSOAPProtocol()).createMessage();
		MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
		SOAPHeader soapHeader = soapMessage.getSOAPHeader();
		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
		SOAPBody body = envelope.getBody();
        
        //Prepare Message
		compileMimeHeaders(mimeHeaders, invocation);
        compileNamespace(envelope, invocation);
        compileSoapHeader(soapHeader, invocation);
        compileSoapBody(body, invocation);
        
        //Save Message
        soapMessage.saveChanges();
 
        //Print the entire request message
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
 
        return soapMessage;
	}
	
	private void compileMimeHeaders(MimeHeaders headers, Invocation invocation) {
		headers.setHeader("SOAPAction", getWebservice().getTargetNamespace() + invocation.getOperation().getName());
		headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        headers.setHeader("Connection", "Keep-Alive");
	}
	
	private void compileNamespace(SOAPEnvelope envelope, Invocation invocation) throws SOAPException {
        envelope.addNamespaceDeclaration(NAMESPACE_PREFIX, getWebservice().getTargetNamespace());
	}
	
	private void compileSoapHeader(SOAPHeader header, Invocation invocation) throws SOAPException {
		//TODO: Preare soapHeader
	}

	private void compileSoapBody(SOAPBody body, Invocation invocation) throws SOAPException {
		//SOAP Operaton
		SOAPElement operation = null;
//		SOAPElement operation = body.addChildElement(new QName(getSoapBean().getTargetNamespace(), getSoapBean().getOperation()));
		if(getWebservice().getTargetNamespace() != null && !"".equals(getWebservice().getTargetNamespace()))
			operation = body.addChildElement(new QName(NAMESPACE_PREFIX + ":" + invocation.getOperation().getName()));
		else
			operation = body.addChildElement(new QName(invocation.getOperation().getName()));
			
		//Prepare operation structure
        compileSoapOperation(operation, invocation.getOperation().getInput(), invocation.getInput());
	}
	
	/**
	 * Compile SOAPOperation envelope based on part structure and schema value
	 * @param scope
	 * @param structure
	 * @param input
	 * @throws SOAPException
	 */
	private void compileSoapOperation(SOAPElement scope, Part structure, SchemaValue input) throws SOAPException{
		for(String key : structure.getParametersSchemaNames()){
			//Get native value
			Object nativeValue = input.getParameterValue(key);
			
			//Get schema from part structure
			Schema schema = structure.getParameterSchema(key);
			
			//Prepare element
			SOAPElement element = addElement(scope, schema);
			
			//Handle
			if(nativeValue instanceof SchemaValue) 
				//Nested
				compileSoapOperation(element, schema, (SchemaValue) nativeValue);
			else 
				//Plain
				setElementValue(element, schema, nativeValue);
		}
	}
	
	/**
	 * Compile SOAPOperation envelope based on schema model and schema values
	 * @param scope
	 * @param innerSchema
	 * @param input
	 * @throws SOAPException
	 */
	private void compileSoapOperation(SOAPElement scope, Schema innerSchema, SchemaValue input) throws SOAPException{
		Schema next = innerSchema;
		do{
			//Get native value
			Object nativeValue = input.getParameterValue(next.getName());
			
			//Prepare element
			SOAPElement element = addElement(scope, next);
			
			//Handle
			if(nativeValue instanceof SchemaValue)
				//Nested
				compileSoapOperation(element, next.getInner(), (SchemaValue) nativeValue);
			else
				//Plain
				setElementValue(element, next, nativeValue);
			
		} while((next = next.getNext()) != null);
	}
	
	/**
	 * Add child element to parent
	 * @param parent
	 * @param schema
	 * @return SOAPElement
	 * @throws SOAPException
	 */
	private SOAPElement addElement(SOAPElement parent, Schema schema) throws SOAPException{
		Namespace namespace = schema.getNamespace();
		return parent.addChildElement(schema.getName(), namespace.getPrefix(), namespace.getURI());
	}
	
	/**
	 * Set an element value based on schema and nativeValue
	 * @param element
	 * @param schema
	 * @param nativeValue
	 */
	private void setElementValue(SOAPElement element, Schema schema, Object nativeValue){
		//Retrieve the field type
		XSDType type = schema.getType();
		
		//Convert it to a hard text value
		String textValue = type.getConverter().toString(nativeValue);
		
		//Set element value
		element.setValue(textValue);
	}
		
	
	/**
	 * Compile the soap operation using the input and model structure
	 * @param operation
	 * @param input
	 * @param model
	 * @throws SOAPException
	 */
//	private void compileSoapOperation(SOAPElement operation, List<Schema> model, List<SchemaValue> input) throws SOAPException{
//		//Iterate over the user input data
//		Set<String> keys = input.getAllData().keySet();
//		for(String key : keys){
//			String name = key;
//			
//			//Retrieve the native language value
//			Object nativeValue = input.getData(name);
//			
//			//Prepare node name
////			QName qname = new QName(getSoapBean().getTargetNamespace(), name);
//			QName qname = new QName(name);
//			SOAPElement inputElement = operation.addChildElement(qname);
//			
//			//Check if its a complex value then handle it recursively
//			if(nativeValue instanceof TO)
//				compileSoapOperation(inputElement, (TO) nativeValue, (TO) model.getData(name));
//			else{
//				//Retrieve the field type
//				XSDType type = (XSDType) model.getData(name);
//				
//				//Convert it to a hard text value
//				String textValue = type.getConverter().toString(nativeValue);
//				
//				//Set element value
//				inputElement.setValue(textValue);
//			}
//			
//		}
//	}
	
	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}
	
	public Instance getWebservice() {
		return webservice;
	}
	
//	public Service getService(String serviceName){
//		for(Object value : getWsdlDefinition().getServices().values())
//			if(value instanceof Service){
//				QName qName = ((Service) value).getQName();
//				if(qName.getLocalPart().equals(serviceName))
//					return (Service) value;
//			}
//		return null;
//	}
//	
//	public List<Service> getServices() {
//		List<Service> services = new ArrayList<>();
//		for(Object value : getWsdlDefinition().getServices().values())
//			if(value instanceof Service)
//				services.add((Service) value);
//		return services;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public Port getPort(String serviceName, String portName){
//		Service service = getService(serviceName);
//		Map<String, Port> ports = service.getPorts();
//		Object[] keySet = (Object[]) ports.keySet().toArray();
//		for(byte c=0;c<keySet.length;c++){
//			String key = keySet[c].toString();
//			if(ports.get(key).getName().equals(portName))
//				return ports.get(key);
//		}
//		return null;
//	}
	
}