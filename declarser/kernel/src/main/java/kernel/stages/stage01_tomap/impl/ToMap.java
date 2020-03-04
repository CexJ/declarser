package kernel.stages.stage01_tomap.impl;

import kernel.tryapi.Try;

import java.util.Map;

public interface ToMap<I, K, V> {
    Try<Map<K, V>> mapping(final I input);
}
