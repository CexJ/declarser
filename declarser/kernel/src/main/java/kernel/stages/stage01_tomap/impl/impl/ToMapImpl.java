package kernel.stages.stage01_tomap.impl.impl;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage01_tomap.impl.destructor.Destructor;
import kernel.stages.stage01_tomap.impl.exceptions.InputMappingException;
import kernel.tryapi.Try;

public final class ToMapImpl<I,K,V> implements ToMap<I, K, V> {

	private final Function<I, Optional<? extends Exception>> inputValidator;
	private final Destructor<I,K,V> destructor;

	private ToMapImpl(
			final Function<I, Optional<? extends Exception>> inputValidator,
			final Destructor<I, K, V> destructor) {
		super();
		this.inputValidator = inputValidator;
		this.destructor = destructor;
	}

	public static <I,K,V> ToMapImpl<I,K,V> of(
			final Function<I, Optional<? extends Exception>> inputValidator,
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
