package br.com.ggdio.wsconsumer.soap.converter;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * String element Converter
 * @author Guilherme Dio
 *
 */
public class StringConverter implements Converter<String> {

	@Override
	public String toObject(String value) {
		return String.valueOf(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}