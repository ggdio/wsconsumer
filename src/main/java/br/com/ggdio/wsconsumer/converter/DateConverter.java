package br.com.ggdio.wsconsumer.converter;

import java.time.LocalDate;

/**
 * Date element Converter
 * @author Guilherme Dio
 *
 */
public class DateConverter implements Converter<LocalDate> {

	@Override
	public LocalDate toObject(String value) {
		return LocalDate.parse(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
