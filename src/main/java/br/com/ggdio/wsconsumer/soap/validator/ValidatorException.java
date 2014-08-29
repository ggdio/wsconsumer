package br.com.ggdio.wsconsumer.soap.validator;

/**
 * Exception type for validators
 * @author Guilherme Dio
 *
 */
public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidatorException() {
		// TODO Auto-generated constructor stub
	}

	public ValidatorException(String message) {
		super(message);
	}
	
	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}