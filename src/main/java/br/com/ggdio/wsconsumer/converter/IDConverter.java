package br.com.ggdio.wsconsumer.converter;

/**
 * ID element Converter
 * @author Guilherme Dio
 *
 */
public class IDConverter implements Converter<String> {

	@Override
	public String toObject(String value) {
		return String.valueOf(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}