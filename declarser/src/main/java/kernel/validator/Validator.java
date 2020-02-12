package kernel.validator;

import utils.tryapi.Try;

public interface Validator<V> {

	Try<V> validate(final V value);

	Validator<?> ok = Try::success;

}
