package declarser;

import declarser.tomap.ToMap;
import declarser.toobject.ToObject;
import declarser.totypedmap.ToTypedMap;
import utils.tryapi.Try;

public final class DeclarserImp<I,K,V,O> implements Declarser<I, O>{

	private final ToMap<I, K, V> toMap;
	private final ToTypedMap<K, V> toTypedMap;
	private final ToObject<K, O> toObject;
	
	private DeclarserImp(ToMap<I, K, V> toMap, ToTypedMap<K, V> toTypedMap, ToObject<K, O> toObject) {
		super();
		this.toMap = toMap;
		this.toTypedMap = toTypedMap;
		this.toObject = toObject;
	}
	
	public static <I,K,V,O> DeclarserImp<I,K,V,O> of(ToMap<I, K, V> toMap, ToTypedMap<K, V> toTypedMap, ToObject<K, O> toObject) {
		return new DeclarserImp<>(toMap, toTypedMap, toObject);
	}

	@Override
	public Try<O> parse(I input) {
		return toMap.apply(input)
				.flatMap(toTypedMap::apply)
				.flatMap(toObject::apply);
	}
}
