package br.com.ggdio.wsconsumer.soap.model;

import br.com.ggdio.wsconsumer.common.model.TO;


/**
 * SOAP Webservice Namespace model
 * @author Guilherme Dio
 *
 */
public class Namespace extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String PREFIX = "PREFIX";
	public static final String URI = "URI";
	
	public Namespace() {
		this("", "");
	}
	
	public Namespace(String prefix, String uri){
		setPrefix(prefix);
		setURI(uri);
	}
	
	public String getPrefix(){
		return getString(PREFIX);
	}
	
	public void setPrefix(String prefix){
		addData(PREFIX, prefix);
	}
	
	public String getURI(){
		return getString(URI);
	}
	
	public void setURI(String uri){
		addData(URI, uri);
	}
	
}