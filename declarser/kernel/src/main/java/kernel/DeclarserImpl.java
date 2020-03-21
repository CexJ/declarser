package kernel;

import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.exceptions.DeclarserException;
import kernel.tryapi.Try;

final class DeclarserImpl<I,K,V,O> implements Declarser<I,K,V,O> {

	private final ToMap<I, K, V> toMap;
	private final ToTypedMap<K, V> toTypedMap;
	private final Combinator<K> combinator;
	private final ToObject<K, O> toObject;
	
	private DeclarserImpl(
			final ToMap<I, K, V> toMap,
			final ToTypedMap<K, V> toTypedMap,
			final Combinator<K> combinator,
			final ToObject<K, O> toObject) {
		super();
		this.toMap = toMap;
		this.toTypedMap = toTypedMap;
		this.combinator = combinator;
		this.toObject = toObject;
	}
	
	static <I,K,V,O> DeclarserImpl<I,K,V,O> of(
			final ToMap<I, K, V> toMap,
			final ToTypedMap<K, V> toTypedMap,
			final Combinator<K> combinator,
			final ToObject<K, O> toObject) {
		return new DeclarserImpl<>(toMap, toTypedMap, combinator, toObject);
	}

	public Try<O> apply(
			final I input) {
		return toMap.mapping(input)
				.map(toTypedMap::typing)
				.flatMap(combinator::combining)
				.flatMap(toObject::gluing)
				.enrichException(ex -> DeclarserException.of(input, ex));
	}
}
