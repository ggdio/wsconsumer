package br.com.ggdio.wsconsumer.main;

import java.util.Scanner;

import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPException;

public class UserApp {

	public static void main(String[] args) throws WSDLException, SOAPException {
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