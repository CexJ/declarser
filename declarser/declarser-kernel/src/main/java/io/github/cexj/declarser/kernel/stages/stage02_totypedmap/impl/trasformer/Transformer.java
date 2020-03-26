package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.function.Function;

public final class Transformer<K,T> {
    private final K key;
    private final Parser<T> function;

    private Transformer(
            final K key,
            final Parser<T> function) {
        this.key = key;
        this.function = function;
    }

    public static <K,T> Transformer<K,T> of(
            final K key,
            final Parser<T> function) {
        return new Transformer<>(key,function);
    }

    public K getKey() {
        return key;
    }

    public Parser<T> getFunction() {
        return function;
    }
}