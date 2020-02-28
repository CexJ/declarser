package kernel.stages.stage04_toobject;

import java.util.Map;
import kernel.stages.stage04_toobject.restructor.Restructor;
import kernel.validation.Validator;
import utils.tryapi.Try;

public final class ToObject<K,O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,O> restructor;
	
	private ToObject(final Validator<O> outputValidator, final Restructor<K,O> restructor) {
		super();
		this.outputValidator = outputValidator;
		this.restructor = restructor;
	}
	
	public static <K,O> ToObject<K,O> of(final Validator<O> outputValidator, final Restructor<K,O> restructor){
		return new ToObject<>(outputValidator, restructor);
	}

	public Try<O> apply(final Map<K,?> map){
		return restructor.restruct(map)
				.continueIf(outputValidator);
	}
}
