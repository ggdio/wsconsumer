package br.com.ggdio.wsconsumer.converter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ListConverter implements Converter<List> {

	@Override
	public List<Object> toObject(String value) {
		return new ArrayList<Object>();
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}