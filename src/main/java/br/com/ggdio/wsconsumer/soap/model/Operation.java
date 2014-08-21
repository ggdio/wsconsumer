package br.com.ggdio.wsconsumer.soap.model;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webservice Operation Model
 * @author Guilherme Dio
 *
 */
public class Operation extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String NAMESPACE = "NAMESPACE";
	public static final String HEADER = "HEADER";
	public static final String INPUT = "INPUT";
	public static final String OUTPUT = "OUTPUT";
	public static final String FAULT = "FAULT";
	
	public Operation() {
		// TODO Auto-generated constructor stub
	}
	
	public Operation(String name, Namespace namespace, Part header, Part input, Part output, Part fault){
		setName(name);
		setNamespace(namespace);
		setHeader(header);
		setInput(input);
		setOutput(output);
		setFault(fault);
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
	
	public Part getHeader(){
		return (Part) getData(HEADER);
	}
	
	public void setHeader(Part header){
		addData(HEADER, header);
	}
	
	public Part getInput(){
		return (Part) getData(INPUT);
	}
	
	public void setInput(Part input){
		addData(INPUT, input);
	}
	
	public Part getOutput(){
		return (Part) getData(INPUT);
	}
	
	public void setOutput(Part output){
		addData(INPUT, output);
	}
	
	public Part getFault(){
		return (Part) getData(FAULT);
	}
	
	public void setFault(Part fault){
		addData(FAULT, fault);
	}
	
}