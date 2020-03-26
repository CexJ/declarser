package io.github.cexj.declarser.kernel;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.ToMap;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import io.github.cexj.declarser.kernel.stages.stage03_combinator.Combinator;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.ToObject;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.function.Function;

public interface Declarser<I,K,V,O> extends Parser<I> {

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
