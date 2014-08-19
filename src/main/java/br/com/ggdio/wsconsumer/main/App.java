package br.com.ggdio.wsconsumer.main;

import br.com.ggdio.wsconsumer.api.SOAPModelDiscovery;
import br.com.ggdio.wsconsumer.api.TO;

public class App {

	public static void main(String args[]) throws Exception {
		TO model = SOAPModelDiscovery.discoverModel("http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx?wsdl", "http://tempuri.org/", "CalcPrecoPrazoWS", "CalcPrecoPrazoWSHttpGet", "CalcPrazo", null, null);
//		SOAPModelDiscovery.discoverModel("http://cybergis.sdsc.edu:8080/opal2/services/LasCountService?wsdl", "http://nbcr.sdsc.edu/opal", "AppService", "AppServicePort", "launchJob", null, null);
	}
	
}