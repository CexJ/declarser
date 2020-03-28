package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer;

import io.github.cexj.declarser.kernel.parsers.Parser;

public final class Transformer<K,T> {
    private final K key;
    private final Parser<T,?> parser;

    private Transformer(
            final K key,
            final Parser<T,?> function) {
        this.key = key;
        this.parser = function;
    }

    public static <K,T> Transformer<K,T> of(
            final K key,
            final Parser<T,?> function) {
        return new Transformer<>(key,function);
    }

    public K getKey() {
        return key;
    }

    public Parser<T,?> getParser() {
        return parser;
    }
}