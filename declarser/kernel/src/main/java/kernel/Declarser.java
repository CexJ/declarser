package kernel;

import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.tryapi.Try;

import java.util.function.Function;

public interface Declarser<I,K,V,O> extends Function<I,Try<?>> {

    static <I,K,V,O> DeclarserImpl<I,K,V,O> of(
            final ToMap<I, K, V> toMap,
            final ToTypedMap<K, V> toTypedMap,
            final Combinator<K> combinator,
            final ToObject<K, O> toObject) {
        return DeclarserImpl.of(
                toMap,
                toTypedMap,
                combinator,
                toObject);
    }

    Try<O> apply(final I input);
}
