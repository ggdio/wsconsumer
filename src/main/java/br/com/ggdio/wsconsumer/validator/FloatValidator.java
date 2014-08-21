package br.com.ggdio.wsconsumer.validator;

public class FloatValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null){
			try{
				Float.parseFloat(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}