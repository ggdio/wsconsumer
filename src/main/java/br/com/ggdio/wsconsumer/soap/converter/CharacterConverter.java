package br.com.ggdio.wsconsumer.soap.converter;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * Character element Converter
 * @author Guilherme Dio
 *
 */
public class CharacterConverter implements Converter<Character> {

	@Override
	public Character toObject(String value) {
		return value.charAt(0);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
