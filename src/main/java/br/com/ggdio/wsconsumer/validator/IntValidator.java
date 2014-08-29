package br.com.ggdio.wsconsumer.validator;

/**
 * Int element Validator
 * @author Guilherme Dio
 *
 */
public class IntValidator implements Validator{

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