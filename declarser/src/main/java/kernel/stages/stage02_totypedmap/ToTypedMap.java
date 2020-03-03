package kernel.stages.stage02_totypedmap;

import utils.tryapi.Try;

import java.util.Map;

public interface ToTypedMap<K, V> {
    Map<K, Try<?>> typing(final Map<K, V> mapInput);
}
