package br.com.ggdio.wsconsumer.soap.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * Date element Converter
 * @author Guilherme Dio
 *
 */
public class DateConverter implements Converter<LocalDate> {

	@Override
	public LocalDate toObject(String value) {
		return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
