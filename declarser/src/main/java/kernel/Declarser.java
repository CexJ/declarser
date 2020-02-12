package kernel;

import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage04_toobject.ToObject;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import utils.tryapi.Try;

import java.util.function.Function;

public final class Declarser<I,K,V,O> {

	private final ToMap<I, K, V> toMap;
	private final ToTypedMap<K, V> toTypedMap;
	private final Combinator<K> combinator;
	private final ToObject<K, O> toObject;
	
	private Declarser(final ToMap<I, K, V> toMap, final ToTypedMap<K, V> toTypedMap, final Combinator<K> combinator, final ToObject<K, O> toObject) {
		super();
		this.toMap = toMap;
		this.toTypedMap = toTypedMap;
		this.combinator = combinator;
		this.toObject = toObject;
	}
	
	public static <I,K,V,O> Declarser<I,K,V,O> of(final ToMap<I, K, V> toMap, final ToTypedMap<K, V> toTypedMap, final Combinator<K> combinator, final ToObject<K, O> toObject) {
		return new Declarser<>(toMap, toTypedMap, combinator, toObject);
	}

	public Try<O> apply(I input) {
		return toMap.apply(input)
				.map(toTypedMap::apply)
				.flatMap(combinator::apply)
				.flatMap(toObject::apply);
	}
}
