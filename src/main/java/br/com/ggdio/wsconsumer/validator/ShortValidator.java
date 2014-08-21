package br.com.ggdio.wsconsumer.validator;

public class ShortValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
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