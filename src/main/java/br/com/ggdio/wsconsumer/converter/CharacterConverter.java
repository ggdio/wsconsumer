package br.com.ggdio.wsconsumer.converter;

public class CharacterConverter implements Converter<Character> {

	@Override
	public Character toObject(String value) {
		return value.toCharArray()[0];
	}

	@Override
	public String toString(Character value) {
		return String.valueOf(value);
	}

}
