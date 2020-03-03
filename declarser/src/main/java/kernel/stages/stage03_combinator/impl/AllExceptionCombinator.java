package kernel.stages.stage03_combinator.impl;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.exceptions.CombiningException;
import kernel.exceptions.GroupedException;
import kernel.stages.stage03_combinator.Combinator;
import utils.tryapi.Try;

import java.util.Map;
import java.util.stream.Collectors;

public final class AllExceptionCombinator<K> implements Combinator<K> {

    private final ParallelizationStrategyEnum parallelizationStrategy;

    private AllExceptionCombinator(
            final ParallelizationStrategyEnum parallelizationStrategy) {
        this.parallelizationStrategy = parallelizationStrategy;
    }

    public static <K> AllExceptionCombinator<K> of(
            final ParallelizationStrategyEnum parallelizationStrategy){
        return new AllExceptionCombinator<>(parallelizationStrategy);
    }

    @Override
    public Try<Map<K, ?>> combining(
            final Map<K, Try<?>> map) {
        final var partition = parallelizationStrategy.exec(map.entrySet().stream())
                .collect(Collectors.partitioningBy(es -> es.getValue().isSuccess()));

        final var success = partition.get(true);

        return Try.success(parallelizationStrategy.exec(success.stream())
                .collect(Collectors.toMap(Map.Entry::getKey, kv -> kv.getValue().getValue())));
    }
}
