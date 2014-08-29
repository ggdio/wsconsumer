package br.com.ggdio.wsconsumer.converter;

/**
 * Base64 element Converter
 * @author Guilherme Dio
 *
 */
public class Base64Converter implements Converter<String> {

	@Override
	public String toObject(String value) {
		return String.valueOf(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}