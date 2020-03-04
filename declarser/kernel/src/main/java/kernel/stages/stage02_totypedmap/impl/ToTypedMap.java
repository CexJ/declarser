package kernel.stages.stage02_totypedmap.impl;

import kernel.tryapi.Try;

import java.util.Map;

public interface ToTypedMap<K, V> {
    Map<K, Try<?>> typing(final Map<K, V> mapInput);
}