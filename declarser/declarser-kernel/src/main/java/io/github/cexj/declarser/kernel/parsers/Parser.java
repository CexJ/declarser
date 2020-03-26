package io.github.cexj.declarser.kernel.parsers;

import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.function.Function;

public interface Parser<T> extends Function<T, Try<?>> {
}
