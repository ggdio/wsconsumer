package br.com.ggdio.wsconsumer.api;

import java.util.ArrayList;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

public class SOAPConsumer {
	
	private static final String KEY_TNS = "tns";
	
	private final String wsdl;
	private final Definition wsdlDefinition;
	
	private String targetNamespace = "";
	private Service targetService;
	private Port targetPort;
	
	private List<Service> detectedServices = new ArrayList<>();

	public SOAPConsumer(String url) throws WSDLException {
		//Read-parse wsdl definition
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		Definition wsdlDef = reader.readWSDL(null, url);
		
		//WSDL
		this.wsdl = url;
		this.wsdlDefinition = wsdlDef;
		
		//Collect services and detected tns
		this.targetNamespace = wsdlDef.getNamespace(KEY_TNS);
		detectedServices.addAll(detectServices(wsdlDef));
	}
	
	private List<Service> detectServices(Definition wsdlDef){
		//Collect services
		List<Service> services = new ArrayList<>();
		for(Object value : wsdlDef.getServices().values())
			if(value instanceof Service)
				services.add((Service) value);
		return services;
	}
	
//	private SOAPMessage createSOAPRequest() throws Exception {
//		MessageFactory messageFactory = MessageFactory.newInstance();
//		SOAPMessage soapMessage = messageFactory.createMessage();
//		SOAPPart soapPart = soapMessage.getSOAPPart();
//
//		String serverURI = "http://tempuri.org/";
//
//		// SOAP Envelope
//		SOAPEnvelope envelope = soapPart.getEnvelope();
//		envelope.addNamespaceDeclaration("web", serverURI);
//
//		// SOAP Body
//		SOAPBody soapBody = envelope.getBody();
//		SOAPElement soapBodyElem = soapBody.addChildElement("FahrenheitToCelsius", "web");
//
//		MimeHeaders headers = soapMessage.getMimeHeaders();
//		headers.addHeader("SOAPAction", serverURI + "FahrenheitToCelsius");
//
//		soapMessage.saveChanges();
//
//		/* Print the request message */
//		System.out.print("Request SOAP Message = ");
//		soapMessage.writeTo(System.out);
//		System.out.println();
//
//		return soapMessage;
//	}

	/**
	 * Method used to print the SOAP Response
	 */
//	private void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
//		System.out.println();
//		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		Transformer transformer = transformerFactory.newTransformer();
//		Source sourceContent = soapResponse.getSOAPPart().getContent();
//		System.out.print("\nResponse SOAP Message = ");
//		StreamResult result = new StreamResult(System.out);
//		transformer.transform(sourceContent, result);
//		System.out.println();
//		System.out.println();
//	}
	
	public List<String> getDetectedServicesNames(){
		List<String> servicesNames = new ArrayList<>();
		getDetectedServices().forEach(v -> servicesNames.add(v.getQName().getLocalPart()));
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

	public List<Service> getDetectedServices() {
		return detectedServices;
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

	public void setDetectedServices(List<Service> detectedServices) {
		this.detectedServices = detectedServices;
	}
	
	
}