package br.com.ggdio.wsconsumer.converter;

import java.time.LocalTime;

public class LocalTimeConverter implements Converter<LocalTime> {

	@Override
	public LocalTime toObject(String value) {
		return LocalTime.parse(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}
}