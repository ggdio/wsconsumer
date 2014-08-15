package br.com.ggdio.wsconsumer.main;

import java.util.Iterator;

import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;

public class App {

	public static void main(String args[]) throws WSDLException {
		SOAPConsumer consumer = new SOAPConsumer("http://wsf.cdyne.com/WeatherWS/Weather.asmx");
		try {
			//User choose the primary information
			final Service targetService = consumer.getServices().get(0);
			@SuppressWarnings("unchecked")
			Iterator<Port> iterator = targetService.getPorts().values().iterator();
			final Port targetPort = iterator.next();
			final Operation targetOperation = ((javax.wsdl.BindingOperation) targetPort.getBinding().getBindingOperations().get(0)).getOperation();
			
//			java.util.Arrays.asList(consumer.getWsdlDefinition().getBindings().values()).get(0);
			/**
			 * User configure the request
			 */
			
			//TEMPCONVERT
//			consumer.setTargetService(targetService);
//			consumer.setTargetPort(targetPort);
//			consumer.setTargetOperation(targetOperation);
//			consumer.setProtocol(SOAPConstants.SOAP_1_1_PROTOCOL);
//			consumer.setParameterValue("Celsius", 13);
			
			//CALCPRAZO
//			consumer.setTargetService(targetService);
//			consumer.setTargetPort(targetPort);
//			consumer.setTargetOperation(targetOperation);
//			consumer.setProtocol(SOAPConstants.SOAP_1_1_PROTOCOL);
//			consumer.setParameterValue("nCdServico", 41106);
//			consumer.setParameterValue("sCepDestino", "04563004");
//			consumer.setParameterValue("sCepOrigem", "09015200");
			
			//TOKEN
//			consumer.setTargetService(targetService);
//			consumer.setTargetPort(targetPort);
//			consumer.setTargetOperation(targetOperation);
//			consumer.setProtocol(SOAPConstants.SOAP_1_1_PROTOCOL);
//			consumer.setParameterValue("email", "ggrdio@gmail.com");
//			consumer.setParameterValue("password", "123456");
			
			//WEATHER
			consumer.setTargetService(targetService);
			consumer.setTargetPort(targetPort);
			consumer.setTargetOperation(targetOperation);
			consumer.setProtocol(SOAPConstants.SOAP_1_1_PROTOCOL);
//			consumer.getParameters()
//			consumer.setParameterValue("ZIP", "10029");
			
			System.out.println("WSDL      = " + consumer.getWsdl());
			System.out.println("TNS       = " + consumer.getTargetNamespace());
			System.out.println("SERVICE   = " + consumer.getTargetService().getQName().getLocalPart());
			System.out.println("PORT      = " + consumer.getTargetPort().getName());
			System.out.println("OPERATION = " + consumer.getTargetOperation().getName());
			
			SOAPMessage response = consumer.invoke();
//			SOAPMessage response = consumer.invoke(new SOAPProvider().getDispatcher(wsdl, targetNamespace, serviceName, portName));

			System.out.print("SOAPResponse:");
			response.writeTo(System.out);
			System.out.println();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}
	}
	
}