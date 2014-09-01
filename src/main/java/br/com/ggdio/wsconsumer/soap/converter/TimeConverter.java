package br.com.ggdio.wsconsumer.soap.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * Time element Converter
 * @author Guilherme Dio
 *
 */
public class TimeConverter implements Converter<LocalTime> {

	@Override
	public LocalTime toObject(String value) {
		return LocalTime.parse(value, DateTimeFormatter.ISO_TIME);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}
}