package kernel.stages.stage01_tomap;

import utils.tryapi.Try;

import java.util.Map;

public interface ToMap<I, K, V> {
    Try<Map<K, V>> mapping(final I input);
}
