package kernel.stages.stage04_toobject.impl.restructor;

import utils.tryapi.Try;

import java.util.Map;

public interface Restructor<K,O> {

    Try<O> restruct(final Map<K,?> input);
}
