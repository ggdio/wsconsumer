package br.com.ggdio.wsconsumer.soap.util;

/**
 * SOAP Utilities
 * @author Guilherme Dio
 *
 */
public final class SOAPUtil {

	/**
	 * Remove namespace alias from value
	 * <p>input  = 'tns:element'
	 * <p>output = 'element' 
	 * @param value
	 * @return
	 */
	public static final String removeNSAlias(String value){
		value = String.valueOf(value);
		return value.substring(value.indexOf(":") + 1);
	}
	
}