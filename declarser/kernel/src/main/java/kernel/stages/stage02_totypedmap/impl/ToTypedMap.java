package kernel.stages.stage02_totypedmap.impl;

import kernel.enums.ParallelizationStrategyEnum;
import kernel.enums.SubsetType;
import kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public interface ToTypedMap<K, V> {

    static <K,V> ToTypedMap<K,V> of(
            final Map<K, Function<V, Try<?>>> mapFunction,
            final SubsetType annotationsSubsetType,
            final ParallelizationStrategyEnum parallelizationStrategy) {
        return ToTypedMapImpl.of(
                mapFunction,
                annotationsSubsetType,
                parallelizationStrategy);
    }

    Map<K, Try<?>> typing(final Map<K, V> mapInput);
}
