package br.com.ggdio.wsconsumer.main;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.wsdl.BindingOperation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;
import br.com.ggdio.wsconsumer.api.SOAPModelDiscovery;
import br.com.ggdio.wsconsumer.api.TO;
import br.com.ggdio.wsconsumer.api.XSDType;
import br.com.ggdio.wsconsumer.api.constant.Constants;
import br.com.ggdio.wsconsumer.soap.model.Part;

/**
 * Console application for tests
 * @author Guilherme Dio
 *
 */
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
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	@SuppressWarnings("rawtypes")
	public void execute() throws WSDLException, SOAPException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		//1 - User input the wsdl url
		final String wsdl = askWsdl();
		TO configuration = new TO();
		configuration.addData(Constants.KEY_WSDL, wsdl);
		Part soapBean = new Part(configuration, new TO(), new TO());
		this.consumer = new SOAPConsumer(soapBean);
		
		//2 - User choose the soap protocol
		final String protocol = askProtocol();
		
		//3 - User choose the service
		final String service = askService();
		
		//4 - User choose the port
		final String port = askPort(service);
		
		//5 - User choose the port
		final String operation = askOperation(service, port);
		
		//6 - Scan wsdl structure
		TO model = SOAPModelDiscovery.discoverModel(wsdl, protocol, "http://api.unicorp.com.br/soap", service, port, operation, null, null);
		soapBean.setModel(model);
		
		//6 - User set the parameters
		TO values = askParts(soapBean.getInputParts());
		
		//7 - User invoke the service
		SOAPMessage response = invoke(values);
		
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
	private String askService(){
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
			return services.get(id-1).getQName().getLocalPart();
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
	private String askPort(String service){
		System.out.println("----");
		System.out.println("Choose one of the ports below:");
		Map<String, Port> ports = getConsumer().getService(service).getPorts();
		Object[] keySet = (Object[]) ports.keySet().toArray();
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
			return ports.get(keySet[id-1]).getName();
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
	private String askOperation(String service, String port){
		System.out.println("----");
		System.out.println("Choose one of the operations below:");
		List<BindingOperation> operations = getConsumer().getPort(service, port).getBinding().getBindingOperations();
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
			return operations.get(id-1).getOperation().getName();
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Ask user for operation parts(parameters)
	 * @return 
	 */
	private TO askParts(TO model){
		System.out.println("----");
		System.out.println("Set the parameters values below:");
		return fillParts(model);
	}
	
	/**
	 * Delegate to fillParts
	 * @param model
	 * @return TO Model(user input data)
	 */
	private TO fillParts(TO model){
		return fillParts(model, 0);
	}
	
	
	/**
	 * Fill parts(params)
	 * @param input
	 * @param level
	 * @return TO Model(user input data)
	 */
	private TO fillParts(TO model, int level){
		TO values = new TO();
		Set<String> keys = model.getAllData().keySet();
		if(level>0) System.out.println();
		for(String key : keys){
			String name = key;
			Object type = model.getData(key);
			for(byte c=0;c<level;c++)
				System.out.print("--");
			if(type instanceof TO){
				System.out.print(name + "]->");
				TO innerValues = fillParts((TO) model.getData(key), level + 1);
				values.addData(name, innerValues);
			}
			else{
				XSDType xsdType = (XSDType) type;
				System.out.print(name + "[" + xsdType.toString() + "]: ");
				String textValue = getScanner().nextLine();
				Object nativeValue = xsdType.getConverter().toObject(textValue);
				values.addData(name, nativeValue);
			}
		}
		return values;
	}
	
	private SOAPMessage invoke(TO values){
		System.out.println("----");
		System.out.println("Press Enter to Invoke....");
		getScanner().nextLine();
		try {
			SOAPMessage response = getConsumer().invoke(values);
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
	
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		try{
			final ConsoleUI app = new ConsoleUI(scanner);
			app.execute();
		}
		finally{
			scanner.close();
		}
	}
	
}