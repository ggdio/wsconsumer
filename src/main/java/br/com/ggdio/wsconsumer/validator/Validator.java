package br.com.ggdio.wsconsumer.validator;

/**
 * Data validation interface
 * @author Guilherme Dio
 *
 * @param <T>
 */
public interface Validator {

	/**
	 * Validates the input information
	 * @param value
	 * @throws ValidatorException - If its not valid, or the operation fails
	 */
	public void validate(String value) throws ValidatorException;
}