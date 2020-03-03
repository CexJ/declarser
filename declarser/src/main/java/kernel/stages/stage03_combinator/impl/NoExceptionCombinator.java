package kernel.stages.stage03_combinator.impl;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage03_combinator.Combinator;
import kernel.exceptions.GroupedException;
import kernel.exceptions.CombiningException;
import utils.tryapi.Try;

import java.util.Map;
import java.util.stream.Collectors;

public final class NoExceptionCombinator<K> implements Combinator<K> {

    private final ParallelizationStrategyEnum parallelizationStrategy;

    private NoExceptionCombinator(
            final ParallelizationStrategyEnum parallelizationStrategy) {
        this.parallelizationStrategy = parallelizationStrategy;
    }

    public static <K> NoExceptionCombinator<K> of(
            final ParallelizationStrategyEnum parallelizationStrategy){
        return new NoExceptionCombinator<>(parallelizationStrategy);
    }

    @Override
    public Try<Map<K, ?>> combining(
            final Map<K, Try<?>> map) {
        final var partition = parallelizationStrategy.exec(map.entrySet().stream())
                    .collect(Collectors.partitioningBy(es -> es.getValue().isSuccess()));

        final var success = partition.get(true);
        final var failure = partition.get(false);

        return failure.isEmpty() ? Try.success(parallelizationStrategy.exec(success.stream())
                                      .collect(Collectors.toMap(Map.Entry::getKey, kv -> kv.getValue().getValue())))
                                 : Try.fail(GroupedException.of(failure.stream()
                                      .map(kv -> CombiningException.of(kv.getKey(), kv.getValue().getException()))
                                      .collect(Collectors.toList())));
    }
}
