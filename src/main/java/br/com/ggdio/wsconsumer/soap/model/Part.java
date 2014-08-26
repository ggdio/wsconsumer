package br.com.ggdio.wsconsumer.soap.model;

import java.util.Set;

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
		this("", new TO());
	}
	
	public Part(String name, TO parametersSchema){
		addData(NAME, name);
		addData(PARAMETERS_SCHEMA, parametersSchema);
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name){
		addData(NAME, name);
	}
	
	public Set<String> getParametersSchemaNames(){
		return ((TO) getData(PARAMETERS_SCHEMA)).getAllData().keySet();
	}
	
	public Schema getParameterSchema(String parameterName){
		return (Schema) ((TO) getData(PARAMETERS_SCHEMA)).getData(parameterName);
	}
	
	public void putParameterSchema(String parameterName, Schema schema){
		((TO) getData(PARAMETERS_SCHEMA)).addData(parameterName, schema);
	}
	
}