package br.com.ggdio.wsconsumer.soap.invoke;

import java.util.List;

import br.com.ggdio.wsconsumer.api.TO;
import br.com.ggdio.wsconsumer.soap.model.Operation;
import br.com.ggdio.wsconsumer.soap.model.Port;
import br.com.ggdio.wsconsumer.soap.model.Service;

public class Invocation extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String SERVICE = "SERVICE";
	public static final String PORT = "PORT";
	public static final String OPERATION = "OPERATION";
	public static final String INPUT = "INPUT";
	
	public Invocation() {
		// TODO Auto-generated constructor stub
	}
	
	public Invocation(Service service, Port port, Operation operation, List<ParameterValue> input) {
		// TODO Auto-generated constructor stub
	}
	
	public Service getService(){
		return (Service) getData(SERVICE);
	}
	
	public void setService(Service service){
		addData(SERVICE, service);
	}
	
}