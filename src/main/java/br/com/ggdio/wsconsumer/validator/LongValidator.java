package br.com.ggdio.wsconsumer.validator;

public class LongValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				Long.parseLong(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}