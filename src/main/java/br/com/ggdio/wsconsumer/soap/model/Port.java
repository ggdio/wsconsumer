package br.com.ggdio.wsconsumer.soap.model;

import java.util.ArrayList;
import java.util.List;

/**
 * SOAP Webservice Port model
 * @author Guilherme Dio
 *
 */
public class Port extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String NAMESPACE = "NAMESPACE";
	public static final String OPERATIONS = "OPERATIONS";
	
	public Port() {
		this("", new Namespace(), new ArrayList<Operation>());
	}
	
	public Port(String name, Namespace namespace, List<Operation> operations){
		setName(name);
		setNamespace(namespace);
		setOperations(operations);
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name){
		addData(NAME, name);
	}
	
	public Namespace getNamespace(){
		return (Namespace) getData(NAMESPACE);
	}
	
	public void setNamespace(Namespace namespace){
		addData(NAME, namespace);
	}
	
	@SuppressWarnings("unchecked")
	public List<Operation> getOperations(){
		return (List<Operation>) getData(OPERATIONS);
	}
	
	public void setOperations(List<Operation> operations){
		addData(OPERATIONS, operations);
	}
	
}