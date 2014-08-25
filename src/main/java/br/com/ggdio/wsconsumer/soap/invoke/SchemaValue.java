package br.com.ggdio.wsconsumer.soap.invoke;

import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * SOAP Webserivce Schema Value model
 * @author Guilherme Dio
 *
 */
public class SchemaValue extends Schema {

	private static final long serialVersionUID = 1L;
	
	public static final String VALUE = "VALUE";
	
	public SchemaValue() {
		// TODO Auto-generated constructor stub
	}
	
	public SchemaValue(Schema schema, Object value) {
		setValue(value);
	}
	
	public Object getValue(){
		return getData(VALUE);
	}
	
	public void setValue(Object value){
		addData(VALUE, value);
	}
	
}