package br.com.ggdio.wsconsumer.converter;

/**
 * Complex element Converter
 * @author Guilherme Dio
 *
 */
public class ComplexConverter implements Converter<Object> {

	@Override
	public Object toObject(String value) {
		return value;
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}