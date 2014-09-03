package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;

/**
 * Long element Validator
 * @author Guilherme Dio
 *
 */
public class LongValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				Long.parseLong(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}