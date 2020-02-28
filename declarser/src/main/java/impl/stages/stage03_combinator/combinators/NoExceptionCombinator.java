package impl.stages.stage03_combinator.combinators;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage03_combinator.Combinator;
import utils.exceptions.GroupedException;
import utils.exceptions.LabeledException;
import utils.tryapi.Try;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class NoExceptionCombinator<K> implements Combinator<K> {

    private final ParallelizationStrategyEnum parallelizationStrategy;

    private NoExceptionCombinator(final ParallelizationStrategyEnum parallelizationStrategy) {
        this.parallelizationStrategy = parallelizationStrategy;
    }

    public static <K> NoExceptionCombinator<K> of(final ParallelizationStrategyEnum parallelizationStrategy){
        return new NoExceptionCombinator<>(parallelizationStrategy);
    }

    @Override
    public Try<Map<K, ?>> combining(final Map<K, Try<?>> map) {
        List<LabeledException> mapExceptions = parallelizationStrategy.exec(map.entrySet().stream())
                .filter(kv -> kv.getValue().isFailure())
                .map(kv -> LabeledException.of(kv.getKey(), kv.getValue().getException()))
                .collect(Collectors.toList());
        return mapExceptions.isEmpty() ? Try.success(parallelizationStrategy.exec(map.entrySet().stream())
                                                                            .collect(Collectors.toMap(
                                                                                    Map.Entry::getKey,
                                                                                    kv -> kv.getValue().getValue())))
                                       : Try.fail(GroupedException.of(mapExceptions));
    }
}
