package io.github.cexj.declarser.kernel.stages.stage01_tomap.impl;

import java.util.Map;

import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.destructor.Destructor;
import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.exceptions.InputMappingException;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

final class ToMapImpl<I,K,V> implements ToMap<I, K, V> {

	private final Validator<I> inputValidator;
	private final Destructor<I,K,V> destructor;

	private ToMapImpl(
			final Validator<I> inputValidator,
			final Destructor<I, K, V> destructor) {
		super();
		this.inputValidator = inputValidator;
		this.destructor = destructor;
	}

	static <I,K,V> ToMapImpl<I,K,V> of(
			final Validator<I> inputValidator,
			final Destructor<I, K, V> destructor) {
		return new ToMapImpl<>(inputValidator, destructor);
	}

	@Override
	public Try<Map<K,V>> mapping(
			final I input){
		return Try.success(input)
				.continueIf(inputValidator)
				.flatMap(destructor::destruct)
				.enrichException( ex -> InputMappingException.of(input, ex));

	}

}
