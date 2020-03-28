package io.github.cexj.declarser.kernel.stages.stage04_toobject.impl;

import java.util.Map;

import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.exceptions.OutputGluingException;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.restructor.Restructor;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

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
