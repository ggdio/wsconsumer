package br.com.ggdio.wsconsumer.soap.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.ggdio.wsconsumer.common.converter.Converter;

/**
 * List element Converter
 * @author Guilherme Dio
 *
 */
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