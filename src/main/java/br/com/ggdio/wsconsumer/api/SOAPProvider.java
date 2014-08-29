package br.com.ggdio.wsconsumer.api;

import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

/**
 * SOAP Provider
 * @author Guilherme Dio
 *
 */
public class SOAPProvider {

	/**
	 * Get a SOAP Dispatcher
	 * @param wsdl - The wsdl document URI
	 * @param namespace - The service wsdl
	 * @param servicename - The service name
	 * @param portName - The port name
	 * @return
	 */
	public Dispatch<SOAPMessage> getDispatcher(String wsdl, String namespace, String servicename, String portName) {
		try {
			// create a representation of the service
			Service service = Service.create(new URL(wsdl), new QName(namespace, servicename));
			final Iterator<QName> ports = service.getPorts();
			QName methodToBeCalled = null;

			// Select the port to be called
			if (portName == null)
				methodToBeCalled = ports.next();
			else
				while (methodToBeCalled == null || !methodToBeCalled.getLocalPart().equals(portName))
					methodToBeCalled = ports.next();

			//Prepare dispatcher
			return createDispatcher(service, methodToBeCalled);
		} catch (Exception e) {
			throw new RuntimeException("Error preparing dispatcher", e);
		}
	}

	/**
	 * Create a new SOAP Dispatcher
	 * @param service - The target service
	 * @param methodToBeCalled - The method QName
	 * @return
	 */
	private Dispatch<SOAPMessage> createDispatcher(Service service, QName methodToBeCalled) {
		Dispatch<SOAPMessage> dispatcher = service.createDispatch(methodToBeCalled, SOAPMessage.class, Service.Mode.MESSAGE);
		return dispatcher;
	}

}
