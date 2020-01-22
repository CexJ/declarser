package declarser.validator;

import utils.tryapi.Try;

public interface Validator<V> {

	Try<V> validate(V value);
	
	static Validator<?> ok = v -> Try.success(v);
	
}
