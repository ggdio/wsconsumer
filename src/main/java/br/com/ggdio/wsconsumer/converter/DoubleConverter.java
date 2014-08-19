package br.com.ggdio.wsconsumer.converter;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double toObject(String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String toString(Double value) {
		return String.valueOf(value);
	}

}
