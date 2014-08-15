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
	
	private static final String KEY_TNS = "tns";
	
	private final String wsdl;
	private final Definition wsdlDefinition;
	
	private final TO configuration = new TO();
	private final TO parameters = new TO();
	
	//List of services
	private List<Service> services = new ArrayList<>();

	public SOAPConsumer(String url) throws WSDLException {
		if(url == null || url.equals(""))
			throw new NullPointerException("URL must not be null or blank");
		
		//WSDL Treatment
		if(!url.toLowerCase().endsWith("?wsdl"))
			url = url.concat("?wsdl");
		
		//Read-parse wsdl definition
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		Definition wsdlDef = reader.readWSDL(null, url);
		
		//WSDL
		this.wsdl = url;
		this.wsdlDefinition = wsdlDef;
		
		//Collect services and detected tns
		setTargetNamespace(wsdlDef.getNamespace(KEY_TNS));
		services.addAll(detectServices(wsdlDef));
		
		setParts(new ArrayList<Part>());
	}
	
	private List<Service> detectServices(Definition wsdlDef){
		//Collect services
		List<Service> services = new ArrayList<>();
		for(Object value : wsdlDef.getServices().values())
			if(value instanceof Service)
				services.add((Service) value);
		return services;
	}
	
	public SOAPMessage invoke() throws SOAPException, IOException {
		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		try{
			return SOAPConnectionFactory.newInstance().createConnection().call(compileRequest(), getWsdl());
		}
		finally{
			connection.close();
		}
    }
	
	public SOAPMessage invoke(Dispatch<SOAPMessage> dispatcher) throws SOAPException, IOException {
		return dispatcher.invoke(compileRequest());
    }
	
	
	private SOAPMessage compileRequest() throws SOAPException, IOException {
        //Create message
		SOAPMessage soapMessage = MessageFactory.newInstance(getProtocol()).createMessage();
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
		SOAPElement operation = body.addChildElement(new QName(getTargetNamespace(), getTargetOperation().getName()));
        
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
        envelope.addNamespaceDeclaration(KEY_TNS, getTargetNamespace());
	}

	private void compileHeaders(MimeHeaders headers) {
		headers.setHeader("SOAPAction", getTargetNamespace() + getTargetOperation().getName());
		headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        headers.setHeader("Connection", "Keep-Alive");
	}

	public List<String> getDetectedServicesNames(){
		List<String> servicesNames = new ArrayList<>();
		getServices().forEach(v -> servicesNames.add(v.getQName().getLocalPart()));
		return servicesNames;
	}
	
	public TO getConfiguration() {
		return configuration;
	}
	
	public TO getParameters() {
		return parameters;
	}
	
	public String getProtocol() {
		return getConfiguration().getString(Constants.KEY_SOAP_PROTOCOL);
	}
	
	public String getWsdl() {
		return wsdl;
	}

	public Definition getWsdlDefinition() {
		return wsdlDefinition;
	}

	public String getTargetNamespace() {
		return getConfiguration().getString(Constants.KEY_TARGET_NAMESPACE);
	}

	public Service getTargetService() {
		return (Service) getConfiguration().getData(Constants.KEY_TARGET_SERVICE);
	}
	
	public Port getTargetPort() {
		return (Port) getConfiguration().getData(Constants.KEY_TARGET_PORT);
	}

	public List<Service> getServices() {
		return services;
	}
	
	@SuppressWarnings("unchecked")
	public List<Part> getParts() {
		return (List<Part>) getConfiguration().getData(Constants.KEY_PARTS);
	}
	
	public Operation getTargetOperation() {
		return (Operation) getConfiguration().getData(Constants.KEY_TARGET_OPERATION);
	}
	
	public void setProtocol(String protocol) {
		getConfiguration().addData(Constants.KEY_SOAP_PROTOCOL, protocol);
	}

	public void setTargetNamespace(String targetNamespace) {
		getConfiguration().addData(Constants.KEY_TARGET_NAMESPACE, targetNamespace);
	}

	public void setTargetService(Service targetService) {
		getConfiguration().addData(Constants.KEY_TARGET_SERVICE, targetService);
	}

	public void setTargetPort(Port targetPort) {
		getConfiguration().addData(Constants.KEY_TARGET_PORT, targetPort);
	}

	public void setServices(List<Service> detectedServices) {
		this.services = detectedServices;
	}
	
	protected void setParts(List<Part> parts) {
		getConfiguration().addData(Constants.KEY_PARTS, parts);
	}
	
	protected void addPart(Part part){
		if(part != null)
			getParts().add(part);
	}
	
	public void setTargetOperation(Operation targetOperation) {
		prepareParts(targetOperation);
		getConfiguration().addData(Constants.KEY_TARGET_OPERATION, targetOperation);
	}

	@SuppressWarnings("unchecked")
	private void prepareParts(Operation targetOperation) {
		getParts().clear();
		getParameters().clear();
		Map<String, Part> parts = targetOperation.getInput().getMessage().getParts();
		for(String key : parts.keySet())
			addPart(parts.get(key));
	}
	
}