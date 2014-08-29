package br.com.ggdio.wsconsumer.validator;

/**
 * Decimal element Validator
 * @author Guilherme Dio
 *
 */
public class DecimalValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				Double.parseDouble(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}