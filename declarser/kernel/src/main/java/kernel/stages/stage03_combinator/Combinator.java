package kernel.stages.stage03_combinator;

import java.util.Map;

import kernel.tryapi.Try;

public interface Combinator<K> {

	Try<Map<K,?>> combining(final Map<K,Try<?>> value);
	
}