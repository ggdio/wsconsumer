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

/**
 * SOAP Consumer utility
 * @author Guilherme Dio
 *
 */
public class SOAPConsumer {
	
	private static final String NAMESPACE_PREFIX = "tns";
	
	private final Definition wsdlDefinition;
	private final SOAPBean soapBean;
	
	public SOAPConsumer(SOAPBean bean) throws WSDLException {
		//Handle npe
		if(bean == null)
			throw new NullPointerException("SOAPBean must not be null");
		if(bean.getWSDLUrl() == null || bean.getWSDLUrl().equals(""))
			throw new NullPointerException("URL must not be null or blank");
		
		//Fields
		this.wsdlDefinition = WSDLFactory.newInstance().newWSDLReader().readWSDL(null, bean.getWSDLUrl());
		this.soapBean = bean;
	}
	
	public SOAPMessage invoke(TO input) throws SOAPException, IOException {
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		try{
			return SOAPConnectionFactory.newInstance().createConnection().call(compileRequest(input), getSoapBean().getWSDLUrl());
		}
		finally{
			connection.close();
		}
    }
	
	public SOAPMessage invoke(Dispatch<SOAPMessage> dispatcher, TO input) throws SOAPException, IOException {
		return dispatcher.invoke(compileRequest(input));
    }
	
	private SOAPMessage compileRequest(TO input) throws SOAPException, IOException {
        //Create message
		SOAPMessage soapMessage = MessageFactory.newInstance(getSoapBean().getProtocol()).createMessage();
		MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
		SOAPHeader soapHeader = soapMessage.getSOAPHeader();
		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
		SOAPBody body = envelope.getBody();
        
        //Prepare Message
		compileMimeHeaders(mimeHeaders);
        compileNamespace(envelope);
        compileSoapHeader(soapHeader, input);
        compileSoapBody(body, input);
        
        //Save Message
        soapMessage.saveChanges();
 
        //Print the entire request message
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
 
        return soapMessage;
	}
	
	private void compileMimeHeaders(MimeHeaders headers) {
		headers.setHeader("SOAPAction", getSoapBean().getTargetNamespace() + getSoapBean().getOperation());
		headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        headers.setHeader("Connection", "Keep-Alive");
	}
	
	private void compileNamespace(SOAPEnvelope envelope) throws SOAPException {
        envelope.addNamespaceDeclaration(NAMESPACE_PREFIX, getSoapBean().getTargetNamespace());
	}
	
	private void compileSoapHeader(SOAPHeader header, TO input) throws SOAPException {
		//TODO: Preare soapHeader
	}

	private void compileSoapBody(SOAPBody body, TO input) throws SOAPException {
		//SOAP Operaton
		SOAPElement operation = null;
//		SOAPElement operation = body.addChildElement(new QName(getSoapBean().getTargetNamespace(), getSoapBean().getOperation()));
		if(getSoapBean().getTargetNamespace() != null || "".equals(getSoapBean().getTargetNamespace()))
			operation = body.addChildElement(new QName(NAMESPACE_PREFIX + ":" + getSoapBean().getOperation()));
		else
			operation = body.addChildElement(new QName(getSoapBean().getOperation()));
			
        compileSoapOperation(operation, input);
	}
	
	/**
	 * Delegate to compileSoapOperation below
	 * @param operation
	 * @param input
	 * @throws SOAPException
	 */
	private void compileSoapOperation(SOAPElement operation, TO input) throws SOAPException{
		compileSoapOperation(operation, input, getSoapBean().getInputParts());
	}
	
	/**
	 * Compile the soap operation using the input and model structure
	 * @param operation
	 * @param input
	 * @param model
	 * @throws SOAPException
	 */
	private void compileSoapOperation(SOAPElement operation, TO input, TO model) throws SOAPException{
		//Iterate over the user input data
		Set<String> keys = input.getAllData().keySet();
		for(String key : keys){
			String name = key;
			
			//Retrieve the native language value
			Object nativeValue = input.getData(name);
			
			//Prepare node name
//			QName qname = new QName(getSoapBean().getTargetNamespace(), name);
			QName qname = new QName(name);
			SOAPElement inputElement = operation.addChildElement(qname);
			
			//Check if its a complex value then handle it recursively
			if(nativeValue instanceof TO)
				compileSoapOperation(inputElement, (TO) nativeValue, (TO) model.getData(name));
			else{
				//Retrieve the field type
				XSDType type = (XSDType) model.getData(name);
				
				//Convert it to a hard text value
				String textValue = type.getConverter().toString(nativeValue);
				
				//Set element value
				inputElement.setValue(textValue);
			}
			
		}
	}
	
	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}
	
	public SOAPBean getSoapBean() {
		return soapBean;
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