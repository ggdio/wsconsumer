package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;

public class SOAPConsumer {
	
	private static final String NAMESPACE_PREFIX = "tns";
	
	private final Definition wsdlDefinition;
	private final SOAPBean soapBean;
	
	public SOAPConsumer(SOAPBean bean) throws WSDLException {
		//Handle npe
		if(bean == null || bean.equals(""))
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
		MimeHeaders headers = soapMessage.getMimeHeaders();
		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
		SOAPBody body = envelope.getBody();
        
        //Prepare Message
        compileHeaders(headers);
        compileNamespace(envelope);
        compileBody(body);
        
        //Save Message
        soapMessage.saveChanges();
 
        //Print the entire request message
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
 
        return soapMessage;
	}

	private void compileBody(SOAPBody body) throws SOAPException {
		//SOAP Operaton
		SOAPElement operation = body.addChildElement(new QName(getSoapBean().getTargetNamespace(), getSoapBean().getOperation()));
        
        //SOAP Parameters
        for(Part part : getParts()){
        	QName qName = new QName(getTargetNamespace(), part.getName());
			SOAPElement element = operation.addChildElement(qName);
			Object value = getParameters().getData(part.getName());
			if(value != null)
				element.setValue(String.valueOf(value));
        }
	}

	private void compileNamespace(SOAPEnvelope envelope) throws SOAPException {
        envelope.addNamespaceDeclaration(NAMESPACE_PREFIX, getSoapBean().getTargetNamespace());
	}

	private void compileHeaders(MimeHeaders headers) {
		headers.setHeader("SOAPAction", getSoapBean().getTargetNamespace() + getSoapBean().getOperation());
		headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        headers.setHeader("Connection", "Keep-Alive");
	}

	@SuppressWarnings("unchecked")
	private void prepareParts(Operation targetOperation) {
		Map<String, Part> parts = targetOperation.getInput().getMessage().getParts();
		for(String key : parts.keySet())
			addPart(parts.get(key));
	}
	
	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}
	
	public SOAPBean getSoapBean() {
		return soapBean;
	}
	
}