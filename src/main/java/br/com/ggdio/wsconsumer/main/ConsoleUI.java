package br.com.ggdio.wsconsumer.main;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.wsdl.BindingOperation;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;

public class ConsoleUI {
	
	private final Scanner scanner;
	private SOAPConsumer consumer;
	
	public ConsoleUI(Scanner scanner) {
		this.scanner = scanner;
	}
	
	/**
	 * Execute the console app
	 * @throws WSDLException
	 * @throws SOAPException 
	 */
	@SuppressWarnings("rawtypes")
	public void execute() throws WSDLException, SOAPException {
		//1 - User input the wsdl url
		final String wsdl = askWsdl();
		setConsumer(new SOAPConsumer(wsdl));
		
		//2 - User choose the soap protocol
		final String protocol = askProtocol();
		getConsumer().setProtocol(protocol);
		
		//3 - User choose the service
		final Service targetService = askService();
		getConsumer().setTargetService(targetService);
		
		//4 - User choose the port
		final Port targetPort = askPort();
		getConsumer().setTargetPort(targetPort);
		
		//5 - User choose the port
		final Operation targetOperation = askOperation();
		getConsumer().setTargetOperation(targetOperation);
		
		//6 - User set the parameters
		askParts();
		
		//7 - User invoke the service
		SOAPMessage response = invoke();
		
		//8 - User configure the response
		Iterator childElements = response.getSOAPBody().getChildElements();
		while(childElements.hasNext()){
			SOAPElement element = (SOAPElement) childElements.next();
			System.out.println(element.getNodeName());
		}
	}
	
	/**
	 * Ask user for WSDL url
	 * @return
	 */
	private String askWsdl(){
		System.out.print("WSDL URL: ");
		return getScanner().nextLine();
	}
	
	/**
	 * Ask user for service protocol
	 * @return
	 */
	private String askProtocol(){
		System.out.println("----");
		System.out.println("Choose one of the protocols below:");
		
		System.out.println("[1] - " + SOAPConstants.SOAP_1_1_PROTOCOL);
		System.out.println("[2] - " + SOAPConstants.SOAP_1_2_PROTOCOL);
		System.out.println();
		System.out.print("Protocol ID: ");
		
		String inputStr = getScanner().nextLine();
		if(String.valueOf(inputStr).equals("1"))
			return SOAPConstants.SOAP_1_1_PROTOCOL;
		if(String.valueOf(inputStr).equals("2"))
			return SOAPConstants.SOAP_1_2_PROTOCOL;
		
		System.out.println("Wrong Protocol ID");
		System.exit(1);
		return null;
	}
	
	/**
	 * Ask user for target service
	 * @return
	 */
	private Service askService(){
		System.out.println("----");
		System.out.println("Choose one of the services below:");
		List<Service> services = getConsumer().getServices();
		for(byte c=0;c<services.size();c++){
			System.out.print("[" + (c+1) + "] - ");
			System.out.println(services.get(c).getQName().getLocalPart());
		}
		System.out.println();
		System.out.print("Service ID: ");
		String inputStr = getScanner().nextLine();
		try{
			byte id = Byte.parseByte(inputStr);
			if(id <= 0 || id > services.size())
				throw new IllegalArgumentException("Wrong Service ID");
			return services.get(id-1);
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Ask user for target port
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Port askPort(){
		System.out.println("----");
		System.out.println("Choose one of the ports below:");
		Map<String, Port> ports = getConsumer().getTargetService().getPorts();
		Object[] keySet = (Object[]) getConsumer().getTargetService().getPorts().keySet().toArray();
		for(byte c=0;c<keySet.length;c++){
			String key = keySet[c].toString();
			System.out.print("[" + (c+1) + "] - ");
			System.out.println(ports.get(key).getName());
		}
		System.out.println();
		System.out.print("Port ID: ");
		String inputStr = getScanner().nextLine();
		try{
			byte id = Byte.parseByte(inputStr);
			if(id <= 0 || id > keySet.length)
				throw new IllegalArgumentException("Wrong Port ID");
			return ports.get(keySet[id-1]);
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Ask user for target operation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Operation askOperation(){
		System.out.println("----");
		System.out.println("Choose one of the operations below:");
		List<BindingOperation> operations = getConsumer().getTargetPort().getBinding().getBindingOperations();
		for(byte c=0;c<operations.size();c++){
			System.out.print("[" + (c+1) + "] - ");
			System.out.println(operations.get(c).getName());
		}
		System.out.println();
		System.out.print("Service ID: ");
		String inputStr = getScanner().nextLine();
		try{
			byte id = Byte.parseByte(inputStr);
			if(id <= 0 || id > operations.size())
				throw new IllegalArgumentException("Wrong Operation ID");
			return operations.get(id-1).getOperation();
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Ask user for operation parts(parameters)
	 */
	private void askParts(){
		System.out.println("----");
		System.out.println("Set the parameters values below:");
		for(Part part : getConsumer().getParts()){
			String name = part.getName();
			System.out.print(name + ": ");
			String inputStr = getScanner().nextLine();
			getConsumer().getParameters().addData(name, inputStr);
//			getConsumer().setParameterValue(D, inputStr);
		}
	}
	
	private SOAPMessage invoke(){
		System.out.println("----");
		System.out.println("Press Enter to Invoke....");
		getScanner().nextLine();
		try {
			SOAPMessage response = getConsumer().invoke();
			System.out.println("----");
			System.out.println("Server Response: ");
			response.writeTo(System.out);
			return response;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	public Scanner getScanner() {
		return scanner;
	}
	
	public SOAPConsumer getConsumer() {
		return consumer;
	}
	
	public void setConsumer(SOAPConsumer consumer) {
		this.consumer = consumer;
	}
	
}