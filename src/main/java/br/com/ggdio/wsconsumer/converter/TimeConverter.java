package br.com.ggdio.wsconsumer.converter;

import java.time.LocalTime;

/**
 * Time element Converter
 * @author Guilherme Dio
 *
 */
public class TimeConverter implements Converter<LocalTime> {

	@Override
	public LocalTime toObject(String value) {
		return LocalTime.parse(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}
}