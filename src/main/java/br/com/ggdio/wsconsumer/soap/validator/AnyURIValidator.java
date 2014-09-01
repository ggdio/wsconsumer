package br.com.ggdio.wsconsumer.soap.validator;

import java.net.URL;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;

/**
 * AnyURI element Validator
 * @author Guilherme Dio
 *
 */
public class AnyURIValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null)
			try {
				new URL(value).toURI();
			}
			catch(Exception e){
				throw new ValidatorException();
			}
	}
	
}