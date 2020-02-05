package declarser.typedvalue;

public class TypedValue<O> {
	
	private final O value;
	private final Class<O> clazz;
	
	private TypedValue(O value, Class<O> clazz) {
		super();
		this.value = value;
		this.clazz = clazz;
	}

	public static <O> TypedValue<O> of(O value, Class<O> clazz) {
		return new TypedValue<O>(value, clazz);
	}
	
	public O getValue() {
		return value;
	}

	public Class<O> getClazz() {
		return clazz;
	}

	
	
}
