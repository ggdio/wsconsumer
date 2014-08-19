package br.com.ggdio.wsconsumer.converter;

public class LongConverter implements Converter<Long> {

	@Override
	public Long toObject(String value) {
		return Long.parseLong(value);
	}

	@Override
	public String toString(Long value) {
		return String.valueOf(value);
	}

}
