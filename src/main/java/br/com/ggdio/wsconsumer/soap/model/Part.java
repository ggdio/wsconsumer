package br.com.ggdio.wsconsumer.soap.model;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webservice Part Model
 * @author Guilherme Dio
 *
 */
public class Part extends TO{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String SCHEMA = "SCHEMA"; 

	public Part() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name){
		addData(NAME, name);
	}
	
	public Schema getSchema(){
		return (Schema) getData(SCHEMA);
	}
	
	public void setSchema(Schema schema){
		addData(SCHEMA, schema);
	}
	
}