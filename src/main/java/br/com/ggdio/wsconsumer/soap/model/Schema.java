package br.com.ggdio.wsconsumer.soap.model;

import br.com.ggdio.wsconsumer.api.TO;
import br.com.ggdio.wsconsumer.api.XSDType;

/**
 * SOAP Webservice Schema model
 * @author Guilherme Dio
 *
 */
public class Schema extends TO{

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String NAMESPACE = "NAMESPACE";
	public static final String TYPE = "TYPE";
	public static final String INNER = "INNER";
	public static final String NEXT = "NEXT";
	
	public Schema() {
		this("", new Namespace(), XSDType.STRING, null, null);
	}
	
	public Schema(String name, Namespace namespace, XSDType type, Schema inner, Schema next) {
		setName(name);
		setNamespace(namespace);
		setType(type);
		setInner(inner);
		setNext(next);
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
		addData(NAMESPACE, namespace);
	}
	
	public XSDType getType(){
		return (XSDType) getData(TYPE);
	}
	
	public void setType(XSDType type){
		addData(TYPE, type);
	}
	
	public Schema getInner(){
		return (Schema) getData(INNER);
	}
	
	public void setInner(Schema inner){
		addData(INNER, inner);
	}
	
	public Schema getNext(){
		return (Schema) getData(NEXT);
	}
	
	public void setNext(Schema inner){
		addData(NEXT, inner);
	}
	
}