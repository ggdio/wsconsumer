package br.com.ggdio.wsconsumer.validator;

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