package kernel.stages.stage04_toobject;

import utils.tryapi.Try;

import java.util.Map;

public interface ToObject<K, O> {
    Try<O> gluing(Map<K, ?> map);
}
