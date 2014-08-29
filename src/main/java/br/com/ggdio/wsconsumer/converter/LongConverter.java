package br.com.ggdio.wsconsumer.converter;

/**
 * Long element Converter
 * @author Guilherme Dio
 *
 */
public class LongConverter implements Converter<Long> {

	@Override
	public Long toObject(String value) {
		return Long.parseLong(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
