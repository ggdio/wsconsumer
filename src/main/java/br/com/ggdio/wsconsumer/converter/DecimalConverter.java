package br.com.ggdio.wsconsumer.converter;

public class DecimalConverter implements Converter<Double> {

	@Override
	public Double toObject(String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
