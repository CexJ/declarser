package kernel.stages.stage02_totypedmap;

import utils.tryapi.Try;

import java.util.Map;

public interface ToTypedMap<K, V> {
    Map<K, Try<?>> typing(Map<K, V> mapInput);
}
