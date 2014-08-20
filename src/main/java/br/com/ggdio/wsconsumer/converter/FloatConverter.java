package br.com.ggdio.wsconsumer.converter;

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
