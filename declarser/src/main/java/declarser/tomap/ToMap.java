package declarser.tomap;

import java.util.Map;

import declarser.tomap.destructor.Destructor;
import declarser.validator.Validator;
import utils.tryapi.Try;

public final class ToMap<I,K,V>{

	private final Validator<I> inputValidator;
	private final Validator<Map<K,V>> mapValidator;
	private final Destructor<I,K,V> destructor;
	
	private ToMap(final Validator<I> inputValidator,
			final Validator<Map<K, V>> mapValidator, final Destructor<I, K, V> destructor) {
		super();
		this.inputValidator = inputValidator;
		this.mapValidator = mapValidator;
		this.destructor = destructor;
	}

	private static <I,K,V> ToMap<I,K,V> of(final Validator<I> inputValidator, 
			final Validator<Map<K, V>> mapValidator, final Destructor<I, K, V> destructor) {
		return new ToMap<>(inputValidator, mapValidator, destructor);
	}
	
	public Try<Map<K,V>> apply(final I input){
		return inputValidator.validate(input).map(destructor::destruct).flatMap(mapValidator::validate);
	}

	public static class Builder<I,K,V>{
		
		@SuppressWarnings("unchecked")
		private Validator<I> inputValidator = (Validator<I>) Validator.ok;
		@SuppressWarnings("unchecked")
		private Validator<Map<K,V>> mapValidator = (Validator<Map<K,V>>) Validator.ok;
			
		public Builder<I,K,V> withInputValidator(final Validator<I> inputValidator){
			this.inputValidator = inputValidator;
			return this;
		}
		
		
		public Builder<I,K,V> withMapValidator(final Validator<Map<K,V>> mapValidator){
			this.mapValidator = mapValidator;
			return this;
		}
		
		public ToMap<I,K,V> build(final Destructor<I,K,V> destructor){
			return ToMap.of(inputValidator, mapValidator, destructor);
		}
	}
	
}
