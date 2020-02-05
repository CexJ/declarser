package declarser.typedvalue;

import java.util.function.Function;

public class TypedFunction<V,O> {
	
	private final Function<V,O> function;
	private final Class<O> clazz;
	
	private TypedFunction(Function<V,O> function, Class<O> clazz) {
		super();
		this.function = function;
		this.clazz = clazz;
	}

	public static <V,O> TypedFunction<V,O> of(Function<V,O> function, Class<O> clazz) {
		return new TypedFunction<V,O>(function, clazz);
	}
	
	public TypedValue<O> apply(V value) {
		return TypedValue.of(function.apply(value), clazz);
	}

	public Class<O> getClazz() {
		return clazz;
	}

	
	
}
