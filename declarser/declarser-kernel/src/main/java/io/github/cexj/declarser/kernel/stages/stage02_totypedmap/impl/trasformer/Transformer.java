package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer;

import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.function.Function;

public final class Transformer<K,T> {
    private final K key;
    private final Function<T, Try<?>> function;

    private Transformer(
            final K key,
            final Function<T, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    public static <K,T> Transformer<K,T> of(
            final K key,
            final Function<T, Try<?>> function) {
        return new Transformer<>(key,function);
    }

    public K getKey() {
        return key;
    }

    public Function<T, Try<?>> getFunction() {
        return function;
    }
}