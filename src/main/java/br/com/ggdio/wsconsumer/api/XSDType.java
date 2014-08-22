package br.com.ggdio.wsconsumer.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.ggdio.wsconsumer.converter.AnyURIConverter;
import br.com.ggdio.wsconsumer.converter.Base64Converter;
import br.com.ggdio.wsconsumer.converter.BooleanConverter;
import br.com.ggdio.wsconsumer.converter.ByteConverter;
import br.com.ggdio.wsconsumer.converter.CharacterConverter;
import br.com.ggdio.wsconsumer.converter.Converter;
import br.com.ggdio.wsconsumer.converter.DateConverter;
import br.com.ggdio.wsconsumer.converter.DateTimeConverter;
import br.com.ggdio.wsconsumer.converter.DecimalConverter;
import br.com.ggdio.wsconsumer.converter.DoubleConverter;
import br.com.ggdio.wsconsumer.converter.FloatConverter;
import br.com.ggdio.wsconsumer.converter.IDConverter;
import br.com.ggdio.wsconsumer.converter.IDRefsConverter;
import br.com.ggdio.wsconsumer.converter.IntConverter;
import br.com.ggdio.wsconsumer.converter.IntegerConverter;
import br.com.ggdio.wsconsumer.converter.ListConverter;
import br.com.ggdio.wsconsumer.converter.LongConverter;
import br.com.ggdio.wsconsumer.converter.NonNegativeIntegerConverter;
import br.com.ggdio.wsconsumer.converter.ObjectConverter;
import br.com.ggdio.wsconsumer.converter.ShortConverter;
import br.com.ggdio.wsconsumer.converter.StringConverter;
import br.com.ggdio.wsconsumer.converter.TimeConverter;
import br.com.ggdio.wsconsumer.converter.TimestampConverter;
import br.com.ggdio.wsconsumer.validator.AnyURIValidator;
import br.com.ggdio.wsconsumer.validator.Base64Validator;
import br.com.ggdio.wsconsumer.validator.BooleanValidator;
import br.com.ggdio.wsconsumer.validator.ByteValidator;
import br.com.ggdio.wsconsumer.validator.CharacterValidator;
import br.com.ggdio.wsconsumer.validator.DateTimeValidator;
import br.com.ggdio.wsconsumer.validator.DateValidator;
import br.com.ggdio.wsconsumer.validator.DecimalValidator;
import br.com.ggdio.wsconsumer.validator.DoubleValidator;
import br.com.ggdio.wsconsumer.validator.FloatValidator;
import br.com.ggdio.wsconsumer.validator.IDRefsValidator;
import br.com.ggdio.wsconsumer.validator.IDValidator;
import br.com.ggdio.wsconsumer.validator.IntValidator;
import br.com.ggdio.wsconsumer.validator.IntegerValidator;
import br.com.ggdio.wsconsumer.validator.ListValidator;
import br.com.ggdio.wsconsumer.validator.LongValidator;
import br.com.ggdio.wsconsumer.validator.NonNegativeIntegerValidator;
import br.com.ggdio.wsconsumer.validator.ObjectValidator;
import br.com.ggdio.wsconsumer.validator.ShortValidator;
import br.com.ggdio.wsconsumer.validator.StringValidator;
import br.com.ggdio.wsconsumer.validator.TimeValidator;
import br.com.ggdio.wsconsumer.validator.TimestampValidator;
import br.com.ggdio.wsconsumer.validator.Validator;

/**
 * XSD Types ENUM for handling conversion
 * @author Guilherme Dio
 *
 */
public enum XSDType {

	CHAR("ws.soap.type.char", Character.class, new CharacterValidator(), new CharacterConverter()),
	
	STRING("ws.soap.type.string", String.class, new StringValidator(), new StringConverter()),
	
	SHORT("ws.soap.type.short", Short.class, new ShortValidator(), new ShortConverter()),
	
	INT("ws.soap.type.int", Integer.class, new IntValidator(), new IntConverter()),
	
	INTEGER("ws.soap.type.integer", Integer.class, new IntegerValidator(), new IntegerConverter()),
	
	NONNEGATIVEINTEGER("ws.soap.type.nonnegativeinteger", Integer.class, new NonNegativeIntegerValidator(), new NonNegativeIntegerConverter()),
	
	LONG("ws.soap.type.long", Long.class, new LongValidator(), new LongConverter()),
	
	FLOAT("ws.soap.type.float", Float.class, new FloatValidator(), new FloatConverter()),
	
	DOUBLE("ws.soap.type.double", Double.class, new DoubleValidator(), new DoubleConverter()),
	
	DECIMAL("ws.soap.type.decimal", Double.class, new DecimalValidator(), new DecimalConverter()),
	
	BOOLEAN("ws.soap.type.boolean", Boolean.class, new BooleanValidator(), new BooleanConverter()),
	
	BYTE("ws.soap.type.byte", Byte.class, new ByteValidator(), new ByteConverter()),
	
	BASE64BINARY("ws.soap.type.base64binary", String.class, new Base64Validator(), new Base64Converter()),
	
	DATE("ws.soap.type.date", LocalDate.class, new DateValidator(), new DateConverter()),
	
	TIME("ws.soap.type.time", LocalTime.class, new TimeValidator(), new TimeConverter()),
	
	DATETIME("ws.soap.type.datetime", LocalDateTime.class, new DateTimeValidator(), new DateTimeConverter()),
	
	TIMESTAMP("ws.soap.type.timestamp", LocalDateTime.class, new TimestampValidator(), new TimestampConverter()),
	
	ANYURI("ws.soap.type.anyuri", String.class, new AnyURIValidator(), new AnyURIConverter()),
	
	ID("ws.soap.type.id", String.class, new IDValidator(), new IDConverter()),
	
	IDREFS("ws.soap.type.idrefs", String.class, new IDRefsValidator(), new IDRefsConverter()),
	
	LIST("ws.soap.type.list", List.class, new ListValidator(), new ListConverter()),
	
	COMPLEX("ws.soap.type.complex", Object.class, new ObjectValidator(), new ObjectConverter());
	
	private final String internalKey;
	private final Class<?> nativeType;
	private final Validator validator;
	private final Converter<?> converter;

	private <T extends Object> XSDType(String internalKey, Class<T> nativeType, Validator validator, Converter<T> converter) {
		this.internalKey = internalKey;
		this.nativeType = nativeType;
		this.validator = validator;
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
	
	public String getInternalKey() {
		return internalKey;
	}
	
	public Class<?> getNativeType() {
		return nativeType;
	}
	
	public Validator getValidator() {
		return validator;
	}
	
	public Converter<?> getConverter() {
		return converter;
	}
}