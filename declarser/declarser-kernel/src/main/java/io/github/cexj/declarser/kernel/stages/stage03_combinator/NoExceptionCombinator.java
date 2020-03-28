package io.github.cexj.declarser.kernel.stages.stage03_combinator;

import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import io.github.cexj.declarser.kernel.exceptions.GroupedException;
import io.github.cexj.declarser.kernel.stages.stage03_combinator.exceptions.CombiningException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;
import java.util.stream.Collectors;

final class NoExceptionCombinator<K> implements Combinator<K> {

    private final ParallelizationStrategyEnum parallelizationStrategy;

    private NoExceptionCombinator(
            final ParallelizationStrategyEnum parallelizationStrategy) {
        this.parallelizationStrategy = parallelizationStrategy;
    }

    static <K> NoExceptionCombinator<K> of(
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
