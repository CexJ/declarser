package kernel.stages.stage03_combinator;

import java.util.Map;

import kernel.enums.ParallelizationStrategyEnum;
import kernel.tryapi.Try;

public interface Combinator<K> {

	static <K> Combinator<K> allException(
			final ParallelizationStrategyEnum parallelizationStrategy){
		return AllExceptionCombinator.of(parallelizationStrategy);
	}

	static <K> Combinator<K> noException(
			final ParallelizationStrategyEnum parallelizationStrategy){
		return NoExceptionCombinator.of(parallelizationStrategy);
	}


	Try<Map<K,?>> combining(final Map<K,Try<?>> value);
	
}
