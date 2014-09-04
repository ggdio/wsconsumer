package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * String element Validator
 * @author Guilherme Dio
 *
 */
public class StringValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		//Nothing to validate yet
	}

}
