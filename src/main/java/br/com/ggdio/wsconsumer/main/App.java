package br.com.ggdio.wsconsumer.main;

import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;
import br.com.ggdio.wsconsumer.api.SOAPProvider;

public class App {

	public static void main(String args[]) throws WSDLException {
		SOAPConsumer consumer = new SOAPConsumer("http://www.w3schools.com/Webservices/tempconvert.asmx?wsdl");
		try {
			//User choose the primary information
			final Service targetService = consumer.getDetectedServices().get(0);
			final Port targetPort = (Port) targetService.getPorts().values().iterator().next();
			consumer.setTargetService(targetService);
			consumer.setTargetPort(targetPort);
			
			String wsdl = consumer.getWsdl();
			String targetNamespace = consumer.getTargetNamespace();
			String serviceName = consumer.getTargetService().getQName().getLocalPart();
			String portName = consumer.getTargetPort().getName();
			
			System.out.println("WSDL    = " + consumer.getWsdl());
			System.out.println("TNS     = " + consumer.getTargetNamespace());
			System.out.println("SERVICE = " + consumer.getTargetService().getQName().getLocalPart());
			System.out.println("PORT    = " + consumer.getTargetPort().getName());
			
			SOAPProvider provider = new SOAPProvider();
			Dispatch<SOAPMessage> dispatcher = provider.getDispatcher(wsdl, targetNamespace, serviceName, portName);
			
			consumer.invoke(dispatcher);
			
			// Create SOAP Connection
//			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//
//			SOAPMessage soapResponse;
//
//			soapResponse = soapConnection.call(createSOAPRequest(), URL);
//			printSOAPResponse(soapResponse);
//
//			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}
	}
	
}
