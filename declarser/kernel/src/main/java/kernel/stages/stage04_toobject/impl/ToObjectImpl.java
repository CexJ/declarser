package kernel.stages.stage04_toobject.impl;

import java.util.Map;

import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.validations.Validator;
import kernel.stages.stage04_toobject.impl.exceptions.OutputGluingException;
import kernel.tryapi.Try;

final class ToObjectImpl<K,O> implements ToObject<K, O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,O> restructor;
	
	private ToObjectImpl(
			final Validator<O> outputValidator,
			final Restructor<K,O> restructor) {
		super();
		this.outputValidator = outputValidator;
		this.restructor = restructor;
	}
	
	static <K,O> ToObjectImpl<K,O> of(
			final Validator<O> outputValidator,
			final Restructor<K,O> restructor){
		return new ToObjectImpl<>(outputValidator, restructor);
	}

	@Override
	public Try<O> gluing(
			final Map<K, ?> map){
		return restructor.restruct(map)
				.continueIf(outputValidator)
				.enrichException(ex -> OutputGluingException.of(map, ex));
	}
}
