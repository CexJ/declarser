package kernel.totypedmap.utils;

import utils.tryapi.Try;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ToTypedMapUtils {

    public static <K,V> Function<Map<K,V>, Map<K, Try<?>>> fromFunctionMapToMapFunction(final Map<K, Function<V, Try<?>>> functionMap) {
        return kvMap -> kvMap.entrySet().parallelStream()
                        .map(kv -> (ToTypedMapComposition<K,V>) ToTypedMapComposition.of(kv.getKey(), kv.getValue(), functionMap.get(kv.getKey())))
                        .collect(Collectors.toMap(ToTypedMapComposition::getKey, ToTypedMapComposition::apply));
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

