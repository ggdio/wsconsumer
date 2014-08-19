package br.com.ggdio.wsconsumer.converter;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer toObject(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String toString(Integer value) {
		return String.valueOf(value);
	}

}
