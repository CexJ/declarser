package kernel.stages.stage04_toobject.impl.impl;

import java.util.Map;

import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.validation.Validator;
import kernel.exceptions.OutputGluingException;
import kernel.tryapi.Try;

public final class ToObjectImpl<K,O> implements ToObject<K, O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,O> restructor;
	
	private ToObjectImpl(
			final Validator<O> outputValidator,
			final Restructor<K,O> restructor) {
		super();
		this.outputValidator = outputValidator;
		this.restructor = restructor;
	}
	
	public static <K,O> ToObjectImpl<K,O> of(
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
