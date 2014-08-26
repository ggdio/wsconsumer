package br.com.ggdio.wsconsumer.util;

/**
 * SOAP Utilities
 * @author Guilherme Dio
 *
 */
public final class SOAPUtil {

	public static final String removeNSAlias(String value){
		value = String.valueOf(value);
		return value.substring(value.indexOf(":") + 1);
	}
	
}