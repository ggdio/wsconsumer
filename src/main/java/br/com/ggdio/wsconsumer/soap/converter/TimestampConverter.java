package br.com.ggdio.wsconsumer.soap.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * Timestamp element Converter
 * @author Guilherme Dio
 *
 */
public class TimestampConverter implements Converter<LocalDateTime> {

	@Override
	public LocalDateTime toObject(String value) {
		return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
