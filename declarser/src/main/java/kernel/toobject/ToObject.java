package kernel.toobject;

import java.util.Map;
import java.util.function.UnaryOperator;

import kernel.tomap.ToMap;
import kernel.toobject.restructor.Restructor;
import kernel.validator.Validator;
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
		return restructor.restruct(map).flatMap(outputValidator::validate);
	}

	public ToObject<K,O> changeOutputValidator(final UnaryOperator<Validator<O>> operator){
		return Builder.from(this).withOutputValidator(operator.apply(outputValidator)).build(this.restructor);
	}

	public ToObject<K,O> changeRestructor(final UnaryOperator<Restructor<K,O>> operator){
		return Builder.from(this).build(operator.apply(this.restructor));
	}

	static class Builder<K,O>{

		private Validator<O> outputValidator = (Validator<O>) Validator.ok;

		public static <K,O> Builder<K,O> empty(){
			return new Builder<>();
		}

		public static <K,O> Builder<K,O> of(final Validator<O> outputValidator){
			return new Builder<K,O>().withOutputValidator(outputValidator);
		}

		public static <K,O> Builder<K,O> from(final ToObject<K,O> toObject){
			return Builder.of(toObject.outputValidator);
		}

		public Builder<K,O> withOutputValidator(final Validator<O> outputValidator){
			this.outputValidator = outputValidator;
			return this;
		}

		public ToObject<K,O> build(final Restructor<K,O> restructor){
			return ToObject.of(outputValidator,restructor);
		}
	}
}
