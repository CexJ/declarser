package io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.destructor;

import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;

public interface Destructor<I,K,V> {

	Try<Map<K,V>> destruct(final I input);
}
