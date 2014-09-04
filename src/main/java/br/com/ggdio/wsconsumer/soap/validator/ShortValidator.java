package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * Short element Validator
 * @author Guilherme Dio
 *
 */
public class ShortValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				Short.parseShort(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}