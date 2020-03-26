package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl;

import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.exceptions.MissingFieldFunctionException;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.exceptions.TypingFieldException;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer.Transformer;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class ToTypedMapImpl<K,V> implements ToTypedMap<K, V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;

	private ToTypedMapImpl(
			final Map<K, Parser<V>> functionMap,
			final SubsetType subsetType,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		super();
		this.mapFunction = fromFunctionMapToMapFunction(functionMap, subsetType, parallelizationStrategy);
	}

	private Function<Map<K,V>, Map<K, Try<?>>> fromFunctionMapToMapFunction(
			final Map<K, Parser<V>> functionMap,
			final SubsetType subsetType,
			final ParallelizationStrategyEnum parallelizationStrategy) {
		return kvMap ->
				parallelizationStrategy.exec(kvMap.entrySet().stream()).map( kv  ->
				Optional.ofNullable(functionMap.get(kv.getKey()))
				.map(parserToTypedMapCompositionFunction(kv))
				.or(emptyToTypedMapCompositionFunction(kv))
				.filter(opt -> subsetType.isStrict() || functionMap.get(kv.getKey()) != null))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(ToTypedMapComposition::getKey, ToTypedMapComposition::apply));
	}

	private Supplier<Optional<? extends ToTypedMapComposition<K, V>>> emptyToTypedMapCompositionFunction(Map.Entry<K, V> kv) {
		return () -> Optional.of(
				ToTypedMapComposition.of(
						Transformer.of(kv.getKey(), any -> Try.fail(MissingFieldFunctionException.of(kv.getKey()))),
						kv.getValue()));
	}

	private Function<Parser<V>, ToTypedMapComposition<K, V>> parserToTypedMapCompositionFunction(Map.Entry<K, V> kv) {
		return fun ->
				ToTypedMapComposition.of(
						Transformer.of(kv.getKey(),fun),
						kv.getValue());
	}

	static <K,V> ToTypedMapImpl<K,V> of(
			final Map<K, Parser<V>> mapFunction,
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


