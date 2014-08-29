package br.com.ggdio.wsconsumer.converter;

/**
 * Integer element Converter
 * @author Guilherme Dio
 *
 */
public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer toObject(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
