package kernel.stages.stage02_totypedmap;

import kernel.conf.ParallelizationStrategyEnum;
import utils.tryapi.Try;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ToTypedMap<K,V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;

	private ToTypedMap(final Map<K, Function<V, Try<?>>> functionMap, ParallelizationStrategyEnum parallelizationStrategy) {
		super();
		this.mapFunction = fromFunctionMapToMapFunction(functionMap, parallelizationStrategy);
	}

	public static <K,V> Function<Map<K,V>, Map<K, Try<?>>> fromFunctionMapToMapFunction(final Map<K, Function<V, Try<?>>> functionMap, ParallelizationStrategyEnum parallelizationStrategy) {
		return kvMap -> parallelizationStrategy.exec(kvMap.entrySet().stream())
				.map(kv -> ToTypedMapComposition.of(kv.getKey(), kv.getValue(), functionMap.get(kv.getKey())))
				.collect(Collectors.toMap(ToTypedMapComposition::getKey, ToTypedMapComposition::apply));
	}

	public static <K,V> ToTypedMap<K,V> of(final Map<K, Function<V, Try<?>>> mapFunction, ParallelizationStrategyEnum parallelizationStrategy) {
		return new ToTypedMap<>(mapFunction, parallelizationStrategy);
	}

	public Map<K, Try<?>> typing(final  Map<K,V> mapInput){
		return mapFunction.apply(mapInput);
	}

}

final class ToTypedMapComposition<K,V>{
	private final K key;
	private final V value;
	private final Function<V, Try<?>> typedFunction;

	private ToTypedMapComposition(final K key, final  V value, final Function<V, Try<?>> typedFunction) {
		super();
		this.key = key;
		this.value = value;
		this.typedFunction = typedFunction;
	}

	Try<?> apply(){
		return typedFunction.apply(value);
	}

	K getKey() {
		return key;
	}

	static <K,V> ToTypedMapComposition<K,V> of(final K key, final  V value, final  Function<V, Try<?>> typedFunction){
		return new ToTypedMapComposition<>(key, value, typedFunction);
	}
}


