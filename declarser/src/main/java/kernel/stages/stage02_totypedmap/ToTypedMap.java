package kernel.stages.stage02_totypedmap;

import kernel.conf.ParallelizationStrategyEnum;
import utils.exceptions.MissingFieldFunctionException;
import utils.exceptions.TypingFieldException;
import utils.tryapi.Try;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToTypedMap<K,V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;

	private ToTypedMap(
			final Map<K, Function<V, Try<?>>> functionMap,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		super();
		this.mapFunction = fromFunctionMapToMapFunction(functionMap, parallelizationStrategy);
	}

	public Function<Map<K,V>, Map<K, Try<?>>> fromFunctionMapToMapFunction(
			final Map<K, Function<V, Try<?>>> functionMap,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		return kvMap ->
				parallelizationStrategy.exec(kvMap.entrySet().stream()).map( kv ->
				Optional.ofNullable(functionMap.get(kv.getKey())).map(       f  ->
						ToTypedMapComposition.of(kv.getKey(), kv.getValue(),f))
				.orElse(ToTypedMapComposition.of(kv.getKey(), kv.getValue(),any ->
						Try.fail(MissingFieldFunctionException.of(kv.getKey())))))
				.collect(Collectors.toMap(ToTypedMapComposition::getKey, ToTypedMapComposition::apply));
	}

	public static <K,V> ToTypedMap<K,V> of(
			final Map<K, Function<V, Try<?>>> mapFunction,
			final ParallelizationStrategyEnum parallelizationStrategy) {
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
		return typedFunction.apply(value)
				.enrichException(ex -> TypingFieldException.of(key, value, ex));
	}

	K getKey() {
		return key;
	}

	static <K,V> ToTypedMapComposition<K,V> of(final K key, final  V value, final  Function<V, Try<?>> typedFunction){
		return new ToTypedMapComposition<>(key, value, typedFunction);
	}
}


