package br.com.ggdio.wsconsumer.validator;

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