package br.com.ggdio.wsconsumer.soap.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.wsconsumer.common.validator.Validator;
import br.com.ggdio.wsconsumer.common.validator.ValidatorException;

/**
 * Date element Validator
 * @author Guilherme Dio
 *
 */
public class DateValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		try{
			LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
		}
		catch(Exception e){
			throw new ValidatorException();
		}
	}
	
}