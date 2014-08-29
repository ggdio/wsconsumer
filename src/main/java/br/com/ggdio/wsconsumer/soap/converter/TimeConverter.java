package br.com.ggdio.wsconsumer.soap.converter;

import java.time.LocalTime;

import br.com.ggdio.wsconsumer.common.converter.Converter;

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