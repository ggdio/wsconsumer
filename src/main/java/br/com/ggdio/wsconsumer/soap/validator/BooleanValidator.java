package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;

/**
 * Boolean element Validator
 * @author Guilherme Dio
 *
 */
public class BooleanValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null)
			if(!value.toLowerCase().equals("true") && !value.toLowerCase().equals("false"))
				throw new ValidatorException();
	}

}