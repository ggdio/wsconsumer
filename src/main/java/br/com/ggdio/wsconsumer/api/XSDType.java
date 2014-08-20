package br.com.ggdio.wsconsumer.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import br.com.ggdio.wsconsumer.converter.BooleanConverter;
import br.com.ggdio.wsconsumer.converter.ByteConverter;
import br.com.ggdio.wsconsumer.converter.CharacterConverter;
import br.com.ggdio.wsconsumer.converter.Converter;
import br.com.ggdio.wsconsumer.converter.DoubleConverter;
import br.com.ggdio.wsconsumer.converter.FloatConverter;
import br.com.ggdio.wsconsumer.converter.IntegerConverter;
import br.com.ggdio.wsconsumer.converter.LocalDateConverter;
import br.com.ggdio.wsconsumer.converter.LocalDateTimeConverter;
import br.com.ggdio.wsconsumer.converter.LocalTimeConverter;
import br.com.ggdio.wsconsumer.converter.LongConverter;
import br.com.ggdio.wsconsumer.converter.ShortConverter;
import br.com.ggdio.wsconsumer.converter.StringConverter;

/**
 * XSD Types ENUM for handling conversion
 * @author Guilherme Dio
 *
 */
public enum XSDType {

	CHAR(Character.class, new CharacterConverter()),
	
	STRING(String.class, new StringConverter()),
	
	SHORT(Short.class, new ShortConverter()),
	
	INT(Integer.class, new IntegerConverter()),
	
	INTEGER(Integer.class, new IntegerConverter()),
	
	NONNEGATIVEINTEGER(Integer.class, new IntegerConverter()),
	
	LONG(Long.class, new LongConverter()),
	
	FLOAT(Float.class, new FloatConverter()),
	
	DOUBLE(Double.class, new DoubleConverter()),
	
	DECIMAL(Double.class, new DoubleConverter()),
	
	BOOLEAN(Boolean.class, new BooleanConverter()),
	
	BYTE(Byte.class, new ByteConverter()),
	
	BASE64BINARY(String.class, new StringConverter()),
	
	DATE(LocalDate.class, new LocalDateConverter()),
	
	TIME(LocalTime.class, new LocalTimeConverter()),
	
	DATETIME(LocalDateTime.class, new LocalDateTimeConverter()),
	
	TIMESTAMP(LocalDateTime.class, new LocalDateTimeConverter()),
	
	ANYURI(String.class, new StringConverter()),
	
	ID(String.class, new StringConverter()),
	
	IDREFS(String.class, new StringConverter());
	
	private final Class<?> nativeType;
	private final Converter<?> converter;

	private <T extends Object> XSDType(Class<T> nativeType, Converter<T> converter) {
		this.nativeType = nativeType;
		this.converter = converter;
	}
	
	public static XSDType getXSDType(String typeName){
		return XSDType.valueOf(String.valueOf(typeName).toUpperCase());
	}
	
	public static Boolean exists(String xsdType){
		try{
			getXSDType(xsdType);
			return Boolean.TRUE;
		}
		catch(IllegalArgumentException e){
			return Boolean.FALSE;
		}
	}
	
	public Class<?> getNativeType() {
		return nativeType;
	}
	
	public Converter<?> getConverter() {
		return converter;
	}
}