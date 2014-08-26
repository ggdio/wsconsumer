package br.com.ggdio.wsconsumer.soap.invoke;

import java.util.Set;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webserivce Schema Value model
 * @author Guilherme Dio
 *
 */
public class SchemaValue extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String PARAMETERS_VALUES = "PARAMETERS_VALUES";
	
	public SchemaValue() {
		this(new TO());
	}
	
	public SchemaValue(TO parametersValues) {
		setParametersValues(parametersValues);
	}
	
	public Set<String> getParametersNames(){
		return getParametersValues().getAllData().keySet();
	}
	
	public TO getParametersValues(){
		return (TO) getData(PARAMETERS_VALUES);
	}
	
	public void setParametersValues(TO parametersValues){
		addData(PARAMETERS_VALUES, parametersValues);
	}
	
	public Object getParameterValue(String name){
		return ((TO) getData(PARAMETERS_VALUES)).getData(name);
	}
	
	public void putParameterValue(String name, Object value){
		((TO) getData(PARAMETERS_VALUES)).addData(name, value);
	}
	
	public SchemaValue getInnerParameterValue(String name){
		return (SchemaValue) ((TO) getData(PARAMETERS_VALUES)).getData(name);
	}
	
	public void putInnerParameterValue(String name, SchemaValue inner){
		((TO) getData(PARAMETERS_VALUES)).addData(name, inner);
	}
	
}