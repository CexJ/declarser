package kernel.tomap;

import java.util.Map;
import java.util.function.UnaryOperator;

import kernel.tomap.destructor.Destructor;
import kernel.validator.Validator;
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

	public ToMap<I,K,V> changeInputValidator(final UnaryOperator<Validator<I>> operator){
		return Builder.from(this).withInputValidator(operator.apply(this.inputValidator)).build(this.destructor);
	}

	public ToMap<I,K,V> changeMapValidator(final UnaryOperator<Validator<Map<K,V>>> operator){
		return Builder.from(this).withMapValidator(operator.apply(this.mapValidator)).build(this.destructor);
	}

	public ToMap<I,K,V> changeDestructor(final UnaryOperator<Destructor<I,K,V>> operator){
		return Builder.from(this).build(operator.apply(this.destructor));
	}

	public static class Builder<I,K,V>{

		private Validator<I> inputValidator = (Validator<I>) Validator.ok;
		private Validator<Map<K,V>> mapValidator = (Validator<Map<K,V>>) Validator.ok;

		public static <I,K,V> Builder<I,K,V> empty(){
			return new Builder<>();
		}

		public static <I,K,V> Builder<I,K,V> of(final Validator<I> inputValidator, final Validator<Map<K,V>> mapValidator){
			return new Builder<I,K,V>().withInputValidator(inputValidator).withMapValidator(mapValidator);
		}

		public static <I,K,V> Builder<I,K,V> from(final ToMap<I,K,V> toMap){
			return Builder.of(toMap.inputValidator,toMap.mapValidator);
		}

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
