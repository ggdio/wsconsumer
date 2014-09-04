package br.com.ggdio.wsconsumer.soap.validator;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * String element Validator
 * @author Guilherme Dio
 *
 */
public class EnumerationValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		Schema next = schema.getInner();
		do 
			if(next.getName().equalsIgnoreCase(value)) 
				return; 
		while((next = next.getNext()) != null);
		throw new ValidatorException();
	}

}