package br.com.ggdio.wsconsumer.converter;

/**
 * Int element Converter
 * @author Guilherme Dio
 *
 */
public class IntConverter implements Converter<Integer> {

	@Override
	public Integer toObject(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
