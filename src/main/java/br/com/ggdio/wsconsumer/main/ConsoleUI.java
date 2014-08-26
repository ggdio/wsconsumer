package br.com.ggdio.wsconsumer.main;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import br.com.ggdio.wsconsumer.api.SOAPConsumer;
import br.com.ggdio.wsconsumer.api.SOAPModelDiscovery;
import br.com.ggdio.wsconsumer.api.XSDType;
import br.com.ggdio.wsconsumer.soap.invoke.Invocation;
import br.com.ggdio.wsconsumer.soap.invoke.SchemaValue;
import br.com.ggdio.wsconsumer.soap.model.Instance;
import br.com.ggdio.wsconsumer.soap.model.Operation;
import br.com.ggdio.wsconsumer.soap.model.Part;
import br.com.ggdio.wsconsumer.soap.model.Port;
import br.com.ggdio.wsconsumer.soap.model.Schema;
import br.com.ggdio.wsconsumer.soap.model.Service;

/**
 * Console application for tests
 * @author Guilherme Dio
 *
 */
public class ConsoleUI {
	
	private final Scanner scanner;
	
	private SOAPConsumer consumer;
	private final Invocation invocation = new Invocation();
	
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
	public void execute() throws WSDLException, SOAPException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		//1 - User input wsdl and protocol
		Instance webservice = SOAPModelDiscovery.discoverModel(askWsdl(), askProtocol(), "http://tempuri.org/");
		this.consumer = new SOAPConsumer(webservice);
		
		//3 - User choose the service
		final Service service = askService();
		invocation.setService(service);
		
		//4 - User choose the port
		final Port port = askPort();
		invocation.setPort(port);
		
		//5 - User choose the port
		final Operation operation = askOperation();
		invocation.setOperation(operation);
		
		//6 - User set the parameters
		SchemaValue values = askParts();
		invocation.setInput(values);
		
		//7 - User invoke the service
		SchemaValue response = invoke();
		
		//8 - User configure the response
		presentResult(getInvocation().getOperation().getOutput(), response);
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
		List<Service> services = getConsumer().getWebservice().getServices();
		for(byte c=0;c<services.size();c++){
			System.out.print("[" + (c+1) + "] - ");
			System.out.println(services.get(c).getName());
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
	private Port askPort(){
		System.out.println("----");
		System.out.println("Choose one of the ports below:");
		List<Port> ports = getInvocation().getService().getPorts();
		for(byte c=0;c<ports.size();c++){
			System.out.print("[" + (c+1) + "] - ");
			System.out.println(ports.get(c).getName());
		}
		System.out.println();
		System.out.print("Port ID: ");
		String inputStr = getScanner().nextLine();
		try{
			byte id = Byte.parseByte(inputStr);
			if(id <= 0 || id > ports.size())
				throw new IllegalArgumentException("Wrong Port ID");
			return ports.get(id-1);
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
	private Operation askOperation(){
		System.out.println("----");
		System.out.println("Choose one of the operations below:");
		List<Operation> operations = getInvocation().getPort().getOperations();
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
			return operations.get(id-1);
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
	private SchemaValue askParts(){
		System.out.println("----");
		System.out.println("Set the parameters values below:");
		return fillParts(invocation.getOperation().getInput());
	}
	
	/**
	 * Delegate to fillParts
	 * @param model
	 * @return TO Model(user input data)
	 */
	private SchemaValue fillParts(Part part){
		return fillParts(part, 0);
	}
	
	
	/**
	 * Fill parts(params)
	 * @param input
	 * @param level
	 * @return TO Model(user input data)
	 */
	private SchemaValue fillParts(Part part, int level){
		SchemaValue values = new SchemaValue();
		Set<String> keys = part.getParametersSchemaNames();
		if(level>0) System.out.println();
		for(String key : keys){
			String name = key;
			Schema schema = part.getParameterSchema(key);
			for(byte c=0;c<level;c++)
				System.out.print("--");
			if(schema.getInner() != null){
				//Nested
//				System.out.print(name + "]->");
//				TO innerValues = fillParts((TO) model.getData(key), level + 1);
//				values.addData(name, innerValues);
			}
			else{
				XSDType xsdType = schema.getType();
				System.out.print(name + "[" + xsdType.toString() + "]: ");
				String textValue = getScanner().nextLine();
				Object nativeValue = xsdType.getConverter().toObject(textValue);
				values.putParameterValue(name, nativeValue);
			}
		}
		return values;
	}
	
	private SchemaValue invoke(){
		System.out.println("----");
		System.out.println("Press Enter to Invoke....");
		getScanner().nextLine();
		try {
			SchemaValue response = getConsumer().invoke(getInvocation());
			System.out.println("----");
			System.out.println();
			System.out.println("Server Response: ");
			System.out.println();
			return response;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Compile SOAPOperation envelope based on part structure and schema value
	 * @param scope
	 * @param structure
	 * @param input
	 * @throws SOAPException
	 */
	private void presentResult(Part structure, SchemaValue input) throws SOAPException{
		for(String key : structure.getParametersSchemaNames()){
			//Get native value
			Object nativeValue = input.getParameterValue(key);
			
			//Get schema from part structure
			Schema schema = structure.getParameterSchema(key);
			
			//Handle
			if(nativeValue instanceof SchemaValue) 
				//Nested
				presentResult(schema, (SchemaValue) nativeValue);
			else 
				//Plain
				presentPlainValue(schema, nativeValue);
		}
	}
	
	/**
	 * Compile SOAPOperation envelope based on schema model and schema values
	 * @param scope
	 * @param innerSchema
	 * @param input
	 * @throws SOAPException
	 */
	private void presentResult(Schema innerSchema, SchemaValue input) throws SOAPException{
		Schema next = innerSchema;
		do{
			//Get native value
			Object nativeValue = input.getParameterValue(next.getName());
			
			//Handle
			if(nativeValue instanceof SchemaValue)
				//Nested
				presentResult(next.getInner(), (SchemaValue) nativeValue);
			else
				//Plain
				presentPlainValue(next, nativeValue);
			
		} while((next = next.getNext()) != null);
	}
	
	/**
	 * Set an element value based on schema and nativeValue
	 * @param element
	 * @param schema
	 * @param nativeValue
	 */
	private void presentPlainValue(Schema schema, Object nativeValue){
		//Retrieve the field type
		XSDType type = schema.getType();
		
		//Convert it to a hard text value
		String textValue = type.getConverter().toString(nativeValue);
		
		//Set element value
		System.out.println(schema.getName() + ": " + textValue);
	}
	
	public Scanner getScanner() {
		return scanner;
	}
	
	public SOAPConsumer getConsumer() {
		return consumer;
	}
	
	public Invocation getInvocation() {
		return invocation;
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