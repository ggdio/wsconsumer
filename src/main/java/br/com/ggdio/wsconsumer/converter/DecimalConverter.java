package br.com.ggdio.wsconsumer.converter;

/**
 * Decimal element Converter
 * @author Guilherme Dio
 *
 */
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
