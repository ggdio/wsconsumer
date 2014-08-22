package br.com.ggdio.wsconsumer.soap.model;

import java.util.List;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webservice Part Model
 * @author Guilherme Dio
 *
 */
public class Part extends TO{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String PARAMETERS_SCHEMA = "PARAMETERS_SCHEMA"; 

	public Part() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name){
		addData(NAME, name);
	}
	
	@SuppressWarnings("unchecked")
	public List<Schema> getParametersSchema(){
		return (List<Schema>) getData(PARAMETERS_SCHEMA);
	}
	
	public void setParametersSchema(List<Schema> schema){
		addData(PARAMETERS_SCHEMA, schema);
	}
	
}