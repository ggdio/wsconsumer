package br.com.ggdio.wsconsumer.soap.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;
import br.com.ggdio.wsconsumer.soap.model.Schema;

/**
 * TimeValidator element Validator
 * @author Guilherme Dio
 *
 */
public class TimeValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		try{
			LocalDateTime.parse(value, DateTimeFormatter.ISO_TIME);
		}
		catch(Exception e){
			throw new ValidatorException();
		}
	}

}