package br.com.ggdio.wsconsumer.converter;

/**
 * Short element Converter
 * @author Guilherme Dio
 *
 */
public class ShortConverter implements Converter<Short> {

	@Override
	public Short toObject(String value) {
		return Short.parseShort(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
