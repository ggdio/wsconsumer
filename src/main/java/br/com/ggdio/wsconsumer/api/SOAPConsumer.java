package br.com.ggdio.wsconsumer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;

public class SOAPConsumer {
	
	private static final String KEY_TNS = "tns";
	
	private final String wsdl;
	private final Definition wsdlDefinition;
	
	private String targetNamespace = "";
	private Service targetService;
	private Port targetPort;
	private List<Service> services = new ArrayList<>();
	private List<Operation> operations = new ArrayList<>();

	public SOAPConsumer(String url) throws WSDLException {
		//Read-parse wsdl definition
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		Definition wsdlDef = reader.readWSDL(null, url);
		
		//WSDL
		this.wsdl = url;
		this.wsdlDefinition = wsdlDef;
		
		//Collect services and detected tns
		this.targetNamespace = wsdlDef.getNamespace(KEY_TNS);
		services.addAll(detectServices(wsdlDef));
	}
	
	private List<Service> detectServices(Definition wsdlDef){
		//Collect services
		List<Service> services = new ArrayList<>();
		for(Object value : wsdlDef.getServices().values())
			if(value instanceof Service)
				services.add((Service) value);
		return services;
	}
	
	public SOAPMessage invoke(Dispatch<SOAPMessage> dispatcher) throws SOAPException, IOException {
        SOAPMessage response = dispatcher.invoke(compileRequest());
        return response;
    }
	
	
	private SOAPMessage compileRequest() throws SOAPException, IOException {
		MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
 
 
        //SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(KEY_TNS, getTargetNamespace());
 
        //SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(getTargetService().getQName());
         
        soapMessage.saveChanges();
 
        //Print the request body
        System.out.print("Request SOAP Body = ");
        System.out.println(soapBodyElem.toString());
        
        //Print the entire request message
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
 
        return soapMessage;
	}

	public List<String> getDetectedServicesNames(){
		List<String> servicesNames = new ArrayList<>();
		getServices().forEach(v -> servicesNames.add(v.getQName().getLocalPart()));
		return servicesNames;
	}
	
	public String getWsdl() {
		return wsdl;
	}

	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public Service getTargetService() {
		return targetService;
	}
	
	public Port getTargetPort() {
		return targetPort;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public void setTargetService(Service targetService) {
		this.targetService = targetService;
	}

	public void setTargetPort(Port targetPort) {
		this.targetPort = targetPort;
	}

	public void setServices(List<Service> detectedServices) {
		this.services = detectedServices;
	}
	
}