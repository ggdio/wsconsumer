package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;

/**
 * NonNegativeInteger element Validator
 * @author Guilherme Dio
 *
 */
public class NonNegativeIntegerValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				int intValue = Integer.parseInt(value);
				if(intValue < 0)
					throw new ValidatorException();
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}