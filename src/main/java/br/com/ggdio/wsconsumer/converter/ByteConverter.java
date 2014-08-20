package br.com.ggdio.wsconsumer.converter;

public class ByteConverter implements Converter<Byte> {

	@Override
	public Byte toObject(String value) {
		return Byte.parseByte(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}