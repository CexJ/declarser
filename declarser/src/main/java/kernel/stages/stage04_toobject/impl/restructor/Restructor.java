package kernel.stages.stage04_toobject.impl.restructor;

import kernel.tryapi.Try;

import java.util.Map;

public interface Restructor<K,O> {

    Try<O> restruct(final Map<K,?> input);
}
