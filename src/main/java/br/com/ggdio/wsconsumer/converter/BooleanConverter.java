package br.com.ggdio.wsconsumer.converter;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean toObject(String value) {
		return Boolean.parseBoolean(value);
	}

	@Override
	public String toString(Boolean value) {
		return String.valueOf(value);
	}
	
}