package br.com.ggdio.wsconsumer.validator;

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