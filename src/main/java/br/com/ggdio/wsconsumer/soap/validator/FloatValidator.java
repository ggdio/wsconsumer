package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * Float element Validator
 * @author Guilherme Dio
 *
 */
public class FloatValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				Float.parseFloat(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}