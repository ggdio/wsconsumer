package br.com.ggdio.wsconsumer.main;

import javax.xml.soap.SOAPConstants;

import br.com.ggdio.wsconsumer.api.SOAPModelDiscovery;
import br.com.ggdio.wsconsumer.soap.model.Instance;

public class SOAPDiscoveryTest {

	public static void main(String args[]) throws Exception {
		Instance webservice = SOAPModelDiscovery.discoverModel("http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx?wsdl", SOAPConstants.SOAP_1_1_PROTOCOL, "http://tempuri.org/");
//		TO model = SOAPModelDiscovery.discoverModel("http://cybergis.sdsc.edu:8080/opal2/services/LasCountService?wsdl", "http://nbcr.sdsc.edu/opal", "AppService", "AppServicePort", "launchJob", null, null);
		System.out.println(webservice);
//		webservice.getServices().get(0).getPorts().get(0).getOperations().get(5).getInput().getName()
	}
	
}