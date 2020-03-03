package kernel.stages.stage01_tomap.impl.destructor;

import utils.tryapi.Try;

import java.util.Map;

public interface Destructor<I,K,V> {

	Try<Map<K,V>> destruct(final I input);
}
