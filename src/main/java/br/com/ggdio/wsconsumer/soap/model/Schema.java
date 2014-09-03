package br.com.ggdio.wsconsumer.soap.model;

import br.com.ggdio.wsconsumer.common.model.TO;
import br.com.ggdio.wsconsumer.soap.api.constant.WSDLConstants;


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
	public static final String ELEMENT_FORM_DEFAULT = "ELEMENT_FORM_DEFAULT";
	
	public Schema() {
		this("", new Namespace(), XSDType.STRING, null, null, WSDLConstants.ELEMENT_FORM_DEFAULT_QUALIFIED);
	}
	
	public Schema(String name, Namespace namespace, XSDType type, Schema inner, Schema next, String efd) {
		setName(name);
		setNamespace(namespace);
		setType(type);
		setInner(inner);
		setNext(next);
		setElementFormDefault(efd);
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
	
	public String getElementFormDefault(){
		return getString(ELEMENT_FORM_DEFAULT);
	}
	
	public void setElementFormDefault(String efd){
		addData(ELEMENT_FORM_DEFAULT, efd);
	}
	
	public Boolean isQualified(){
		return getElementFormDefault().equals(WSDLConstants.ELEMENT_FORM_DEFAULT_QUALIFIED);
	}
	
}