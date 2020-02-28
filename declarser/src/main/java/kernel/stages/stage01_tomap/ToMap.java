package kernel.stages.stage01_tomap;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import kernel.stages.stage01_tomap.destructor.Destructor;
import utils.tryapi.Try;

public final class ToMap<I,K,V>{

	private final Function<I, Optional<? extends Exception>> inputValidator;
	private final Destructor<I,K,V> destructor;

	private ToMap(final Function<I, Optional<? extends Exception>> inputValidator,
				  final Destructor<I, K, V> destructor) {
		super();
		this.inputValidator = inputValidator;
		this.destructor = destructor;
	}

	public static <I,K,V> ToMap<I,K,V> of(final Function<I, Optional<? extends Exception>> inputValidator
			, final Destructor<I, K, V> destructor) {
		return new ToMap<>(inputValidator, destructor);
	}

	public Try<Map<K,V>> mapping(final I input){
		return Try.success(input)
				.continueIf(inputValidator)
				.map(destructor::destruct);
	}

}
