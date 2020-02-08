package kernel.combinator;

import java.util.Map;

import utils.tryapi.Try;

public interface Combinator<K> {

	Try<Map<K,?>> apply(final Map<K,Try<?>> value);
	
}
