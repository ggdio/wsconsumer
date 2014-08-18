package br.com.ggdio.wsconsumer.api;

public enum Types { 
	STRING, INT, DOUBLE, DATE;
	public String value() {
		return toString().toLowerCase();
	}
	
}