package kernel.stages.stage04_toobject.restructor;

import utils.tryapi.Try;

import java.util.Map;

public interface Restructor<K,O> {

    Try<O> restruct(final Map<K,?> input);
}
