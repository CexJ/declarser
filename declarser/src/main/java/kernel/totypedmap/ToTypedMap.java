package kernel.totypedmap;

import kernel.totypedmap.utils.ToTypedMapUtils;
import utils.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public final class ToTypedMap<K,V> {

	private final Function<Map<K, V> , Map<K,Try<?>>> mapFunction;
	
	private ToTypedMap(final Map<K, Function<V, Try<?>>> functionMap) {
		super();
		this.mapFunction = ToTypedMapUtils.fromFunctionMapToMapFunction(functionMap);
	}

	public static <K,V> ToTypedMap<K,V> of(final Map<K, Function<V, Try<?>>> mapFunction) {
		return new ToTypedMap<>(mapFunction);
	}

	public Map<K, Try<?>> apply(final  Map<K,V> mapInput){
		return mapFunction.apply(mapInput);
	}

}


