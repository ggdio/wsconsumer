package br.com.ggdio.wsconsumer.main;

import javax.xml.soap.SOAPConstants;

import br.com.ggdio.wsconsumer.api.SOAPModelDiscovery;
import br.com.ggdio.wsconsumer.soap.model.Instance;

public class SOAPDiscoveryTest {

	public static void main(String args[]) throws Exception {
		Instance webservice = SOAPModelDiscovery.discoverModel("http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx?wsdl", SOAPConstants.SOAP_1_1_PROTOCOL, "qualified", "literal");
		System.out.println(webservice);
	}
	
}