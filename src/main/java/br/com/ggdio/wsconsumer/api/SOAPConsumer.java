package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
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

import br.com.ggdio.wsconsumer.soap.invoke.Invocation;
import br.com.ggdio.wsconsumer.soap.invoke.SchemaValue;
import br.com.ggdio.wsconsumer.soap.model.Instance;
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
	
	public SOAPMessage invoke(Invocation invocation) throws SOAPException, IOException {
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		try{
			return SOAPConnectionFactory.newInstance().createConnection().call(compileRequest(invocation), getWebservice().getWSDL());
		}
		finally{
			connection.close();
		}
    }
	
	public SOAPMessage invoke(Dispatch<SOAPMessage> dispatcher, Invocation invocation) throws SOAPException, IOException {
		return dispatcher.invoke(compileRequest(invocation));
    }
	
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
		if(getWebservice().getTargetNamespace() != null || "".equals(getWebservice().getTargetNamespace()))
			operation = body.addChildElement(new QName(NAMESPACE_PREFIX + ":" + invocation.getOperation().getName()));
		else
			operation = body.addChildElement(new QName(invocation.getOperation().getName()));
			
        compileSoapOperation(operation, invocation.getOperation().getInput().getParametersSchema(), invocation.getInput());
	}
	
	private void compileSoapOperation(SOAPElement operation, List<Schema> model, List<SchemaValue> input) throws SOAPException{
		for(SchemaValue value : input){
			
		}
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
	
	public Service getService(String serviceName){
		for(Object value : getWsdlDefinition().getServices().values())
			if(value instanceof Service){
				QName qName = ((Service) value).getQName();
				if(qName.getLocalPart().equals(serviceName))
					return (Service) value;
			}
		return null;
	}
	
	public List<Service> getServices() {
		List<Service> services = new ArrayList<>();
		for(Object value : getWsdlDefinition().getServices().values())
			if(value instanceof Service)
				services.add((Service) value);
		return services;
	}
	
	@SuppressWarnings("unchecked")
	public Port getPort(String serviceName, String portName){
		Service service = getService(serviceName);
		Map<String, Port> ports = service.getPorts();
		Object[] keySet = (Object[]) ports.keySet().toArray();
		for(byte c=0;c<keySet.length;c++){
			String key = keySet[c].toString();
			if(ports.get(key).getName().equals(portName))
				return ports.get(key);
		}
		return null;
	}
	
}