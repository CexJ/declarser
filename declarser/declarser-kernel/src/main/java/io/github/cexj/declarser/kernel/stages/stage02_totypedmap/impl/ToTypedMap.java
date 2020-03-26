package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl;

import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public interface ToTypedMap<K, V> {

    static <K,V> ToTypedMap<K,V> of(
            final Map<K, Parser<V>> mapFunction,
            final SubsetType subsetType,
            final ParallelizationStrategyEnum parallelizationStrategy) {
        return ToTypedMapImpl.of(
                mapFunction,
                subsetType,
                parallelizationStrategy);
    }

    Map<K, Try<?>> typing(final Map<K, V> mapInput);
}
