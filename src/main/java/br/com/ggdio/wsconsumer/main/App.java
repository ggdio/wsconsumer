package br.com.ggdio.wsconsumer.main;


import java.util.Iterator;

import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;
import br.com.ggdio.wsconsumer.api.SOAPProvider;

public class App {

	public static void main(String args[]) throws WSDLException {
		SOAPConsumer consumer = new SOAPConsumer("http://linuxmint-java:8088/mockTempConvertSoap?WSDL");
		try {
			//User choose the primary information
			final Service targetService = consumer.getServices().get(0);
			Iterator<Port> iterator = targetService.getPorts().values().iterator();
			iterator.next();
			final Port targetPort = iterator.next();
			final Operation targetOperation =((javax.wsdl.BindingOperation) targetPort.getBinding().getBindingOperations().get(0)).getOperation(); 
			
			consumer.setTargetService(targetService);
			consumer.setTargetPort(targetPort);
			consumer.setTargetOperation(targetOperation);
			
			
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
	
//	public static void main(String[] args) {
//        WSDLParser parser = new WSDLParser();
// 
//        Definitions defs = parser.parse("http://www.w3schools.com/Webservices/tempconvert.asmx?wsdl");
// 
//        out("-------------- WSDL Details --------------");
//        out("TargenNamespace: \t" + defs.getTargetNamespace());
//        if (defs.getDocumentation() != null) {
//            out("Documentation: \t\t" + defs.getDocumentation());
//        }
//        out("\n");
// 
//        /* For detailed schema information see the FullSchemaParser.java sample.*/
//        out("Schemas: ");
//        for (Schema schema : defs.getSchemas()) {
//            out("  TargetNamespace: \t" + schema.getTargetNamespace());
//        }
//        out("\n");
//         
//        out("Messages: ");
//        for (Message msg : defs.getMessages()) {
//            out("  Message Name: " + msg.getName());
//            out("  Message Parts: ");
//            for (Part part : msg.getParts()) {
//                out("    Part Name: " + part.getName());
//                out("    Part Element: " + ((part.getElement() != null) ? part.getElement() : "not available!"));
//                out("    Part Type: " + ((part.getType() != null) ? part.getType() : "not available!" ));
//                out("");
//            }
//        }
//        out("");
// 
//        out("PortTypes: ");
//        for (PortType pt : defs.getPortTypes()) {
//            out("  PortType Name: " + pt.getName());
//            out("  PortType Operations: ");
//            for (Operation op : pt.getOperations()) {
//                out("    Operation Name: " + op.getName());
//                out("    Operation Input Name: "
//                    + ((op.getInput().getName() != null) ? op.getInput().getName() : "not available!"));
//                out("    Operation Input Message: "
//                    + op.getInput().getMessage().getQname());
//                out("    Operation Output Name: "
//                    + ((op.getOutput().getName() != null) ? op.getOutput().getName() : "not available!"));
//                out("    Operation Output Message: "
//                    + op.getOutput().getMessage().getQname());
//                out("    Operation Faults: ");
//                if (op.getFaults().size() > 0) {
//                    for (Fault fault : op.getFaults()) {
//                        out("      Fault Name: " + fault.getName());
//                        out("      Fault Message: " + fault.getMessage().getQname());
//                    }
//                } else out("      There are no faults available!");
//                 
//            }
//            out("");
//        }
//        out("");
// 
//        out("Bindings: ");
//        for (Binding bnd : defs.getBindings()) {
//            out("  Binding Name: " + bnd.getName());
//            out("  Binding Type: " + bnd.getPortType().getName());
//            out("  Binding Protocol: " + bnd.getBinding().getProtocol());
//            if(bnd.getBinding() instanceof AbstractSOAPBinding) out("  Style: " + (((AbstractSOAPBinding)bnd.getBinding()).getStyle()));
//            out("  Binding Operations: ");
//            for (BindingOperation bop : bnd.getOperations()) {
//                out("    Operation Name: " + bop.getName());
//                if(bnd.getBinding() instanceof AbstractSOAPBinding) {
//                    out("    Operation SoapAction: " + bop.getOperation().getSoapAction());
//                    out("    SOAP Body Use: " + bop.getInput().getBindingElements().get(0).getUse());
//                }
//            }
//            out("");
//        }
//        out("");
// 
//        out("Services: ");
//        for (Service service : defs.getServices()) {
//            out("  Service Name: " + service.getName());
//            out("  Service Potrs: ");
//            for (Port port : service.getPorts()) {
//                out("    Port Name: " + port.getName());
//                out("    Port Binding: " + port.getBinding().getName());
//                out("    Port Address Location: " + port.getAddress().getLocation()
//                    + "\n");
//            }
//        }
//        out("");
//    }
// 
//    private static void out(String str) {
//        System.out.println(str);
//    }
}