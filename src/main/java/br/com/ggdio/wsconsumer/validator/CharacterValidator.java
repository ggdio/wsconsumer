package br.com.ggdio.wsconsumer.validator;

/**
 * Character element Validator
 * @author Guilherme Dio
 *
 */
public class CharacterValidator implements Validator {

	@Override
	public void validate(String value) throws ValidatorException {
		//Check if it contains only 1 character
		if(value != null && value.length() > 1)
			throw new ValidatorException();
	}
	
}