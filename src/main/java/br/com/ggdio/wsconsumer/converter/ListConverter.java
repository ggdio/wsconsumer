package br.com.ggdio.wsconsumer.converter;

import java.util.ArrayList;

public class ListConverter implements Converter<ArrayList> {

	@Override
	public ArrayList toObject(String value) {
		return null;
	}

	@Override
	public String toString(ArrayList value) {
		return String.valueOf(value);
	}

}