package declarser.typedvalue;

import utils.tryapi.Try;

import java.util.Optional;
import java.util.function.Function;

public class TypedFunction<V,O> {
	
	private final Function<Optional<V>, Try<O>> function;
	private final Class<O> clazz;
	
	private TypedFunction(Function<Optional<V>,Try<O>> function, Class<O> clazz) {
		super();
		this.function = function;
		this.clazz = clazz;
	}

	public static <V,O> TypedFunction<V,O> of(Function<Optional<V>,Try<O>> function, Class<O> clazz) {
		return new TypedFunction<>(function, clazz);
	}
	
	public Try<TypedValue<?>> apply(V value) {
		return function.apply(Optional.ofNullable(value)).map(o -> TypedValue.of(o, clazz));
	}

	
	
}
