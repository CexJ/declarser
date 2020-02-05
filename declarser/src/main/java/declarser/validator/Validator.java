package declarser.validator;

import utils.tryapi.Try;

public interface Validator<V> {

	Try<V> validate(final V value);
	
	static Validator<?> ok = v -> Try.success(v);
	
}
