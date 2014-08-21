package br.com.ggdio.wsconsumer.soap.model;

import java.util.List;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webservice Service model
 * @author Guilherme Dio
 *
 */
public class Service extends TO{
	
	private static final long serialVersionUID = 1L;

	private static final String NAME = "NAME";
	private static final String NAMESPACE = "NAMESPACE";
	private static final String PORTS = "PORTS";
	
	public Service() {
		// TODO Auto-generated constructor stub
	}
	
	public Service(String name, Namespace namespace, List<Port> ports){
		setName(name);
		setNamespace(namespace);
		setPorts(ports);
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name) {
		addData(NAME, name);
	}
	
	public Namespace getNamespace(){
		return (Namespace) getData(NAMESPACE);
	}
	
	public void setNamespace(Namespace namespace){
		addData(NAMESPACE, namespace);
	}
	
	@SuppressWarnings("unchecked")
	public List<Port> getPorts(){
		return (List<Port>) getData(PORTS);
	}
	
	public void setPorts(List<Port> ports){
		addData(PORTS, ports);
	}
	
}