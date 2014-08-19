package br.com.ggdio.wsconsumer.converter;

import java.time.LocalDateTime;

public class LocalDateTimeConverter implements Converter<LocalDateTime> {

	@Override
	public LocalDateTime toObject(String value) {
		return LocalDateTime.parse(value);
	}

	@Override
	public String toString(LocalDateTime value) {
		return String.valueOf(value);
	}

}
