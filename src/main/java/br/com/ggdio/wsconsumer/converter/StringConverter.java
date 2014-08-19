package br.com.ggdio.wsconsumer.converter;

public class StringConverter implements Converter<String> {

	@Override
	public String toObject(String value) {
		return String.valueOf(value);
	}

	@Override
	public String toString(String value) {
		return String.valueOf(value);
	}

}