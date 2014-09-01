package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;

/**
 * Integer element Validator
 * @author Guilherme Dio
 *
 */
public class IntegerValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				Integer.parseInt(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}