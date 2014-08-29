package br.com.ggdio.wsconsumer.validator;

/**
 * Double element Validator
 * @author Guilherme Dio
 *
 */
public class DoubleValidator implements Validator{

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