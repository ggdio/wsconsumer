package br.com.ggdio.wsconsumer.converter;

import java.time.LocalDateTime;

public class TimestampConverter implements Converter<LocalDateTime> {

	@Override
	public LocalDateTime toObject(String value) {
		return LocalDateTime.parse(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
