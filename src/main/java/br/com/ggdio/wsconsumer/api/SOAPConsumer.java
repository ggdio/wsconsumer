package br.com.ggdio.wsconsumer.api;

import java.io.IOException;

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
import br.com.ggdio.wsconsumer.soap.model.XSDType;

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
	
	/**
	 * Invoke the webservice
	 * @param invocation
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
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
	
	/**
	 * Invoke the webservice using dispatcher
	 * @param dispatcher
	 * @param invocation
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
	public SOAPMessage invoke(Invocation invocation, Dispatch<SOAPMessage> dispatcher) throws SOAPException, IOException {
		return dispatcher.invoke(compileRequest(invocation));
    }
	
	/**
	 * Parse response based on nodeList and schema structure
	 * @param response
	 * @param structure
	 * @return
	 * @throws SOAPException
	 * @throws XPathExpressionException
	 */
	private SchemaValue parseResponse(SOAPMessage response, Part structure) throws SOAPException, XPathExpressionException {
		return parseResponse(response.getSOAPBody().getChildNodes(), structure);
	}
	
	/**
	 * Parse response based on nodeList and schema structure
	 * @param scope
	 * @param structure
	 * @return
	 * @throws XPathExpressionException
	 */
	private SchemaValue parseResponse(NodeList scope, Part structure) throws XPathExpressionException {
		SchemaValue root = new SchemaValue();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		//Recover the schema by name
		Schema schema = structure.getRootSchema();
		
		if(schema.getInner() != null){
			//NESTED FIELDS
			root.putInnerParameterValue(schema.getName(), parseResponse(xPath, scope, schema.getInner()));
			root.setSchema(schema);
		}
		else{
			//PLAIN FIELD
			resolveValue(xPath, scope, root, schema);
		}
		return root;
	}
	
	/**
	 * Parse response based on nodeList and schema structure
	 * @param xPath
	 * @param scope
	 * @param schema
	 * @return
	 * @throws XPathExpressionException
	 */
	private SchemaValue parseResponse(XPath xPath, NodeList scope, Schema schema) throws XPathExpressionException{
		SchemaValue nested = new SchemaValue();
		Schema next = schema;
		do{
			/*if(next.getType() == XSDType.LIST && next.getInner() != null){
				//MULTIPLE FIELDS
				List<SchemaValue> list = new ArrayList<SchemaValue>();
				nested.putParameterValue(next.getName(), list);
				parseResponse(xPath, scope, schema.getInner(), list);
			}
			else*/ if(next.getInner() != null){
				//NESTED FIELDS
				nested.putInnerParameterValue(next.getName(), parseResponse(xPath, scope, next.getInner()));
				nested.setSchema(next);
			}
			else{
				//PLAIN FIELD
				resolveValue(xPath, scope, nested, next);
			}
		} while((next = next.getNext()) != null);
		return nested;
	}
	
//	private void parseResponse(XPath xPath, NodeList scope, Schema schema, List<SchemaValue> list) throws XPathExpressionException{
//		NodeList items = search(schema.getName(), xPath, scope);
//		for(byte c=0;c<items.getLength();c++){
//			SchemaValue value = new SchemaValue();
//			Node item = items.item(c);
//			setSchemaValue(value, schema, item);
//			list.add(value);
//		}
//	}

	/**
	 * Resolve value
	 * @param xPath
	 * @param scope
	 * @param nested
	 * @param schema
	 * @throws XPathExpressionException
	 */
	private void resolveValue(XPath xPath, NodeList scope, SchemaValue nested, Schema schema) throws XPathExpressionException {
		//Search for the plain node by schema name
		NodeList result = search(schema.getName(), xPath, scope);
		
		//Handle blank or null
		if(result.getLength() == 0){
			nested.putParameterValue(schema.getName(), null);
			return;
		}
		
		//Retrieve the item
		setSchemaValue(nested, schema, result.item(0));
	}

	/**
	 * Set schema value
	 * @param nested
	 * @param schema
	 * @param item
	 */
	private void setSchemaValue(SchemaValue nested, Schema schema, Node item) {
		//Recover the plain value and convert it to a native one
		String plainValue = item.getTextContent();
		Object nativeValue = schema.getType().getConverter().toObject(plainValue);
		
		//Put the parameter native value
		nested.putParameterValue(schema.getName(), nativeValue);
		nested.setSchema(schema);
	}

	/**
	 * Search for an element based on its name
	 * @param typeName
	 * @param xPath
	 * @param scope
	 * @return
	 * @throws XPathExpressionException
	 */
	private NodeList search(String typeName, XPath xPath, NodeList scope) throws XPathExpressionException {
		return (NodeList) xPath.compile("//*[local-name()='" + typeName + "']").evaluate(scope, XPathConstants.NODESET);
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
	
	/**
	 * Compile the request based on invocation bean
	 * @param invocation
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
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
	
	/**
	 * Compile request mime headers
	 * @param headers
	 * @param invocation
	 */
	private void compileMimeHeaders(MimeHeaders headers, Invocation invocation) {
		headers.setHeader("SOAPAction", getWebservice().getTargetNamespace() + invocation.getOperation().getName());
		headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        headers.setHeader("Connection", "Keep-Alive");
	}
	
	/**
	 * Compile namespace
	 * @param envelope
	 * @param invocation
	 * @throws SOAPException
	 */
	private void compileNamespace(SOAPEnvelope envelope, Invocation invocation) throws SOAPException {
        envelope.addNamespaceDeclaration(NAMESPACE_PREFIX, getWebservice().getTargetNamespace());
	}
	
	/**
	 * Compile SOAP Header
	 * @param header
	 * @param invocation
	 * @throws SOAPException
	 */
	private void compileSoapHeader(SOAPHeader header, Invocation invocation) throws SOAPException {
		//TODO: Preare soapHeader
	}

	/**
	 * Compile SOAP Body
	 * @param body
	 * @param invocation
	 * @throws SOAPException
	 */
	private void compileSoapBody(SOAPBody body, Invocation invocation) throws SOAPException {
		//SOAP Operaton
		SOAPElement operation = null;
//		SOAPElement operation = body.addChildElement(new QName(getSoapBean().getTargetNamespace(), getSoapBean().getOperation()));
		if(getWebservice().getTargetNamespace() != null && !"".equals(getWebservice().getTargetNamespace()))
			operation = body.addChildElement(new QName(NAMESPACE_PREFIX + ":" + invocation.getOperation().getName()));
		else
			operation = body.addChildElement(new QName(invocation.getOperation().getName()));
			
		//Prepare operation structure
        compileSoapOperation(operation, invocation.getOperation().getInput().getRootSchema(), invocation.getInput());
	}
	
	/**
	 * Compile SOAPOperation envelope based on part structure and schema value
	 * @param scope
	 * @param structure
	 * @param input
	 * @throws SOAPException
	 */
//	private void compileSoapOperation(SOAPElement scope, Part structure, SchemaValue input) throws SOAPException{
//		//Recover root schema
//		Schema rootSchema = structure.getRootSchema();
//		
//		//Get native value
//		Object nativeValue = input.getParameterValue(rootSchema.getName());
//		
//		//Prepare element
//		SOAPElement element = addElement(scope, rootSchema);
//		
//		//Handle
//		if(nativeValue instanceof SchemaValue) 
//			//Nested
//			compileSoapOperation(element, rootSchema.getInner(), (SchemaValue) nativeValue);
//		else 
//			//Plain
//			setElementValue(element, rootSchema, nativeValue);
//	}
	
	/**
	 * Compile SOAPOperation envelope based on schema model and schema values
	 * @param scope
	 * @param innerSchema
	 * @param input
	 * @throws SOAPException
	 */
	private void compileSoapOperation(SOAPElement scope, Schema schema, SchemaValue input) throws SOAPException{
		//No parameters required
		if(schema == null) 
			return;
		
		Schema upper = null;
		Schema next = schema;
		do{
			//Get native value
			Object nativeValue = input.getParameterValue(next.getName());
			
			//Temporary solution while LIST is not yet supported
			if(next.getType() == XSDType.LIST){
				upper = next;
				next = next.getInner();
				nativeValue = ((SchemaValue) nativeValue).getParameterValue(next.getName());
			}
			else
				upper = null;
			
			//Prepare element
			SOAPElement element = addElement(scope, next);
			
			//Handle
			if(nativeValue instanceof SchemaValue)
				//Nested
				compileSoapOperation(element, next.getInner(), (SchemaValue) nativeValue);
			else
				//Plain
				setElementValue(element, next, nativeValue);
			
			//Temporary solution while LIST is not yet supported
			if(upper != null)
				next = upper;
			
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
	
	/**
	 * Get the WSDL Definition
	 * @return {@link Definition}
	 */
	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}
	
	/**
	 * Get the webservice instance configuration
	 * @return {@link Instance}
	 */
	public Instance getWebservice() {
		return webservice;
	}
	
}