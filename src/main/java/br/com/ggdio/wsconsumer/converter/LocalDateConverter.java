package br.com.ggdio.wsconsumer.converter;

import java.time.LocalDate;

public class LocalDateConverter implements Converter<LocalDate> {

	@Override
	public LocalDate toObject(String value) {
		return LocalDate.parse(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
