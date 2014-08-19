package br.com.ggdio.wsconsumer.converter;

public class ShortConverter implements Converter<Short> {

	@Override
	public Short toObject(String value) {
		return Short.parseShort(value);
	}

	@Override
	public String toString(Short value) {
		return String.valueOf(value);
	}

}
