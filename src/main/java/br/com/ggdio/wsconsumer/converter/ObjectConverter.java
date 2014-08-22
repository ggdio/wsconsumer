package br.com.ggdio.wsconsumer.converter;

public class ObjectConverter implements Converter<Object> {

	@Override
	public Object toObject(String value) {
		return value;
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}