package br.com.ggdio.wsconsumer.converter;

public interface Converter<T> {

	public T toObject(String value);
	public String toString(T value);
}