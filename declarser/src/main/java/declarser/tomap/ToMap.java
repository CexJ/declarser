package declarser.tomap;

import java.util.Map;

import declarser.tomap.destructor.Destructor;
import declarser.validator.Validator;
import utils.tryapi.Try;

public final class ToMap<I,K,V>{

	private final Validator<I> inputValidator;
	private final Validator<Map<K,V>> mapValidator;
	private final Destructor<I,K,V> destructor;
	
	public Try<Map<K,V>> toMap(I input){
		return inputValidator.validate(input).map(destructor::destruct).flatMap(mapValidator::validate);
	}
	
	private ToMap(Validator<I> inputValidator,
			Validator<Map<K, V>> mapValidator, Destructor<I, K, V> destructor) {
		super();
		this.inputValidator = inputValidator;
		this.mapValidator = mapValidator;
		this.destructor = destructor;
	}

	private static <I,K,V> ToMap<I,K,V> of(Validator<I> inputValidator, 
			Validator<Map<K, V>> mapValidator, Destructor<I, K, V> destructor) {
		return new ToMap<>(inputValidator, mapValidator, destructor);
	}
	

	public static class Builder<I,K,V>{
		
		@SuppressWarnings("unchecked")
		private Validator<I> inputValidator = (Validator<I>) Validator.ok;
		@SuppressWarnings("unchecked")
		private Validator<Map<K,V>> mapValidator = (Validator<Map<K,V>>) Validator.ok;
			
		public Builder<I,K,V> withInputValidator(Validator<I> inputValidator){
			this.inputValidator = inputValidator;
			return this;
		}
		
		
		public Builder<I,K,V> withMapValidator(Validator<Map<K,V>> mapValidator){
			this.mapValidator = mapValidator;
			return this;
		}
		
		public ToMap<I,K,V> build(Destructor<I,K,V> destructor){
			return ToMap.of(inputValidator, mapValidator, destructor);
		}
	}
	
}
