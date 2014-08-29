package br.com.ggdio.wsconsumer.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TO implements Serializable {

	private static final long serialVersionUID = 2L;
	
	protected Map<String, Object> fields = null; 
	
	public TO() {
		this.fields = new HashMap<String, Object>();
	}
	
	public TO(Map<String, Object> fields) {
		this();
		if (fields!=null) this.fields.putAll(fields);
	}
	
	protected void finalize() throws Throwable {
		fields = null;
		super.finalize();
	}
	
	public TO addData(String name, Object data) {	
		fields.put(name.toLowerCase(), data);
		return this;
	}
	
	public TO addData(TO data) {
		for(String key : data.getNames()) {
			fields.put(key, data.getData(key));
		}
		
		return this;
	}
	
	public void copyData(TO source, String ... params) {
		for (String key : params) {
			this.addData(key, source.getData(key));
		}
	}
	
	public void clear(){
		fields.clear();
	}
	
	public TO removeData(String name){
		fields.remove(name.toLowerCase());
		return this;
	}
	
	public Map<String, Object> getAllData(){
		return fields;
	}
	
	public Object getData(String name){
		if (fields.containsKey(name.toLowerCase())){
			return fields.get(name.toLowerCase());
		}else{
			return null;
		}
	}
	
	public String getString(String name){
		Object result =  getData(name.toLowerCase()) ;
		if (result == null){
			return "";
		}else{
			return String.valueOf( result );
		}
	}

	public long getLong(String name){
		Object result = getData(name.toLowerCase());
		if (result == null){
			return 0L;
		} else if ("".equals(result)) {
			return 0L;
		}else{
			return Long.parseLong(result.toString());
		}
	}
	
	public int getInteger(String name) {
		Object result = getData(name.toLowerCase());
		if (result == null){
			return 0;
		} else if ("".equals(result)) {
			return 0;
		} else {
			return Integer.parseInt(result.toString());
		}
	}
	
	public String[] getNames(){
		int sequence = 0;
		String[] campos = new String[fields.size()];
		Iterator<String> iterator = fields.keySet().iterator();
		while(iterator.hasNext()) {    
			campos[sequence++] = iterator.next();
		}
		iterator = null;
		return campos;
	}
	
	public boolean containsField(String name){
		return (fields.containsKey(name.toLowerCase()));
	}
	
}
