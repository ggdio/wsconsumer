package br.com.ggdio.wsconsumer.converter;

/**
 * Float element Converter
 * @author Guilherme Dio
 *
 */
public class FloatConverter implements Converter<Float> {

	@Override
	public Float toObject(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
