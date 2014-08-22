package br.com.ggdio.wsconsumer.soap.invoke;

import br.com.ggdio.wsconsumer.api.TO;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * SOAP Parameter Value model
 * @author Guilherme Dio
 *
 */
public class ParameterValue extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String SCHEMA = "SCHEMA";
	public static final String VALUE = "VALUE";
	
	public ParameterValue() {
		// TODO Auto-generated constructor stub
	}
	
	public ParameterValue(Schema schema, Object value) {
		setSchema(schema);
		setValue(value);
	}
	
	public Schema getSchema(){
		return (Schema) getData(SCHEMA);
	}
	
	public void setSchema(Schema schema){
		addData(SCHEMA, schema);
	}
	
	public Object getValue(){
		return getData(VALUE);
	}
	
	public void setValue(Object value){
		addData(VALUE, value);
	}
	
}