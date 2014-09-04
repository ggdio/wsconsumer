package br.com.ggdio.wsconsumer.common.validator;

import br.com.ggdio.wsconsumer.soap.model.Schema;


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
	public void validate(Schema schema, String value) throws ValidatorException;
}