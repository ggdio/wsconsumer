package br.com.ggdio.wsconsumer.common.converter;


/**
 * Data conversion interface
 * @author Guilherme Dio
 *
 * @param <T>
 */
public interface Converter<T> {

	/**
	 * Converts the entire text value to an object
	 * @param value
	 * @return Native Object Value
	 * @throws ConverterException
	 */
	public T toObject(String value) throws ConverterException;
	
	/**
	 * Converts the object to a entire text value
	 * @param value
	 * @return Text Value
	 * @throws ConverterException
	 */
	public String toString(Object value)  throws ConverterException;
}