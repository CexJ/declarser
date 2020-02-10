package kernel.stages.stage02_totypedmap;

import kernel.conf.ParallelizationStrategy;
import kernel.stages.stage02_totypedmap.utils.ToTypedMapUtils;
import utils.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public final class ToTypedMap<K,V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;

	private ToTypedMap(final Map<K, Function<V, Try<?>>> functionMap, ParallelizationStrategy parallelizationStrategy) {
		super();
		this.mapFunction = ToTypedMapUtils.fromFunctionMapToMapFunction(functionMap, parallelizationStrategy);
	}

	public static <K,V> ToTypedMap<K,V> of(final Map<K, Function<V, Try<?>>> mapFunction, ParallelizationStrategy parallelizationStrategy) {
		return new ToTypedMap<>(mapFunction, parallelizationStrategy);
	}

	public Map<K, Try<?>> apply(final  Map<K,V> mapInput){
		return mapFunction.apply(mapInput);
	}
}


