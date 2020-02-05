package declarser.validator;

import java.util.Map;

import utils.tryapi.Try;

public interface ValidatorMap<K,V> {

	Try<Map<K,V>> validate(final Map<K,Try<V>> value);
	
	
}
