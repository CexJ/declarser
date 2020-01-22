package declarser.toobject;

import java.util.Map;

import declarser.tomap.destructor.Destructor;
import declarser.toobject.restructor.Restructor;
import declarser.validator.Validator;
import utils.tryapi.Try;

public class ToObject<K,V,O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,V,O> restructor;

	public Try<Map<K,V>> ToObject(I input){
		return inputValidator.validate(input).map(destructor::destruct).flatMap(mapValidator::validate);
	}
	
	private ToObject(Validator<O> outputValidator) {
		super();
		this.inputValidator = inputValidator;
		this.mapValidator = mapValidator;
		this.destructor = destructor;
	}
	
}
