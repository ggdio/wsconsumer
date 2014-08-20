package br.com.ggdio.wsconsumer.converter;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean toObject(String value) {
		return Boolean.parseBoolean(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}
	
}