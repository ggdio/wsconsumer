package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * Character element Validator
 * @author Guilherme Dio
 *
 */
public class CharacterValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		//Check if it contains only 1 character
		if(value != null && value.length() > 1)
			throw new ValidatorException();
	}
	
}