package br.com.ggdio.wsconsumer.api;

import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

public class SOAPProvider {

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
			return service.createDispatch(methodToBeCalled, SOAPMessage.class, Service.Mode.MESSAGE);
		} catch (Exception e) {
			throw new RuntimeException("Error preparing dispatcher", e);
		}
	}

}
