package declarser.toobject;

import java.util.Map;

import declarser.toobject.restructor.Restructor;
import declarser.typedvalue.TypedValue;
import declarser.validator.Validator;
import utils.tryapi.Try;

public class ToObject<K,O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,O> restructor;
	
	private ToObject(final Validator<O> outputValidator, final Restructor<K,O> restructor) {
		super();
		this.outputValidator = outputValidator;
		this.restructor = restructor;
	}
	
	public static <K,V,O> ToObject<K,O> of(final Validator<O> outputValidator, final Restructor<K,O> restructor){
		return new ToObject<>(outputValidator, restructor);
	}
	
	public Try<O> apply(final Map<K,TypedValue<?>> map){
		return restructor.restruct(map).flatMap(outputValidator::validate);
	}

	
}
