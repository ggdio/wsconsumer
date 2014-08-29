package br.com.ggdio.wsconsumer.validator;

/**
 * Byte element Validator
 * @author Guilherme Dio
 *
 */
public class ByteValidator implements Validator{

	@Override
	public void validate(String value) throws ValidatorException {
		if(value != null)
			try{
				Byte.parseByte(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
	}

}