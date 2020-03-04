package kernel.stages.stage04_toobject.impl;

import kernel.tryapi.Try;

import java.util.Map;

public interface ToObject<K, O> {
    Try<O> gluing(final Map<K, ?> map);
}
