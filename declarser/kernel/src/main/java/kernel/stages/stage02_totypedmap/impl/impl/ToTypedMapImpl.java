package kernel.stages.stage02_totypedmap.impl.impl;

import kernel.enums.ParallelizationStrategyEnum;
import kernel.enums.SubsetType;
import kernel.stages.stage02_totypedmap.impl.exceptions.MissingFieldFunctionException;
import kernel.stages.stage02_totypedmap.impl.exceptions.TypingFieldException;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.tryapi.Try;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ToTypedMapImpl<K,V> implements ToTypedMap<K, V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;

	private ToTypedMapImpl(
			final Map<K, Function<V, Try<?>>> functionMap,
			final SubsetType annotationsSubsetType,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		super();
		this.mapFunction = fromFunctionMapToMapFunction(functionMap, annotationsSubsetType, parallelizationStrategy);
	}

	public Function<Map<K,V>, Map<K, Try<?>>> fromFunctionMapToMapFunction(
			final Map<K, Function<V, Try<?>>> functionMap,
			final SubsetType annotationsSubsetType,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		return kvMap ->
				parallelizationStrategy.exec(kvMap.entrySet().stream()).map( kv  ->
				Optional.ofNullable(functionMap.get(kv.getKey()))
				.map(fun ->
						ToTypedMapComposition.of(
								Transformer.of(kv.getKey(),fun),
								kv.getValue()))
				.or(() -> Optional.of(
						ToTypedMapComposition.of(
								Transformer.of(kv.getKey(), any -> Try.fail(MissingFieldFunctionException.of(kv.getKey()))),
								kv.getValue())))
				.filter(opt -> annotationsSubsetType.isStrict() || functionMap.get(kv.getKey()) != null))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(ToTypedMapComposition::getKey, ToTypedMapComposition::apply));
	}

	public static <K,V> ToTypedMapImpl<K,V> of(
			final Map<K, Function<V, Try<?>>> mapFunction,
			final SubsetType annotationsSubsetType,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		return new ToTypedMapImpl<>(mapFunction, annotationsSubsetType, parallelizationStrategy);
	}

	@Override
	public Map<K, Try<?>> typing(
			final Map<K, V> mapInput){
		return mapFunction.apply(mapInput);
	}

}

final class ToTypedMapComposition<K,V>{
	private final Transformer<K,V> transformer;
	private final V value;

	private ToTypedMapComposition(
			final Transformer<K,V> transformer,
			final  V value) {
		super();
		this.transformer = transformer;
		this.value = value;
	}

	Try<?> apply(){
		return transformer.getFunction().apply(value)
				.enrichException(ex -> TypingFieldException.of(transformer.getKey(), value, ex));
	}

	K getKey() {
		return transformer.getKey();
	}

	static <K,V> ToTypedMapComposition<K,V> of(
			final Transformer<K,V> transformer,
			final  V value){
		return new ToTypedMapComposition<>(transformer, value);
	}
}


