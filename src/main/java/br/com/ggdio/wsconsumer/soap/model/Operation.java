package br.com.ggdio.wsconsumer.soap.model;

import javax.xml.soap.SOAPConstants;

import br.com.ggdio.wsconsumer.common.model.TO;
import br.com.ggdio.wsconsumer.soap.api.constant.WSDLConstants;


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
	public static final String SOAP_PROTOCOL = "SOAP_PROTOCOL";
	public static final String USE = "USE";
	
	public Operation() {
		this("", new Namespace(), new Part(), new Part(), new Part(), new Part(), SOAPConstants.SOAP_1_1_PROTOCOL, WSDLConstants.STYLE_LITERAL);
	}
	
	public Operation(String name, Namespace namespace, Part header, Part input, Part output, Part fault, String soapProtocol, String use){
		setName(name);
		setNamespace(namespace);
		setHeader(header);
		setInput(input);
		setOutput(output);
		setFault(fault);
		setSOAPProtocol(soapProtocol);
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
		return (Part) getData(OUTPUT);
	}
	
	public void setOutput(Part output){
		addData(OUTPUT, output);
	}
	
	public Part getFault(){
		return (Part) getData(FAULT);
	}
	
	public void setFault(Part fault){
		addData(FAULT, fault);
	}
	
	public void setSOAPProtocol(String soapProtocol) {
		addData(SOAP_PROTOCOL, soapProtocol);
	}
	
	public String getSOAPProtocol(){
		return getString(SOAP_PROTOCOL);
	}
	
	public String getUse(){
		return getString(USE);
	}
	
	public void setUse(String use){
		addData(USE, use);
	}
	
}