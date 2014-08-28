package br.com.ggdio.wsconsumer.soap.model;


/**
 * SOAP Webservice Part Model
 * @author Guilherme Dio
 *
 */
public class Part extends TO{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "NAME";
	public static final String ROOT_SCHEMA = "ROOT_SCHEMA"; 

	public Part() {
		this("", null);
	}
	
	public Part(String name, Schema rootSchema){
		addData(NAME, name);
		addData(ROOT_SCHEMA, rootSchema);
	}
	
	public String getName(){
		return getString(NAME);
	}
	
	public void setName(String name){
		addData(NAME, name);
	}
	
//	public Set<String> getParametersSchemaNames(){
//		return ((TO) getData(ROOT_SCHEMA)).getAllData().keySet();
//	}
	
	public Schema getRootSchema(){
		return (Schema) getData(ROOT_SCHEMA);
	}
	
	public void setRootSchema(Schema schema){
		addData(ROOT_SCHEMA, schema);
	}
	
}