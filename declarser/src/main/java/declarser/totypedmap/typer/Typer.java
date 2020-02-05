package declarser.totypedmap.typer;

import java.util.Map;
import java.util.stream.Collectors;

import declarser.typedvalue.TypedFunction;
import declarser.typedvalue.TypedValue;
import utils.tryapi.Try;

public class Typer<K,V> {

	private final Map<K, TypedFunction<V, ?>> mapFunction;
	
	private Typer(Map<K, TypedFunction<V, ?>> mapFunction) {
		super();
		this.mapFunction = mapFunction;
	}

	public static <K,V> Typer<K,V> of(Map<K, TypedFunction<V, ?>> mapFunction) {
		return new Typer<>(mapFunction);
	}

	public Map<K, Try<TypedValue<?>>> type(Map<K,V> mapInput){
		return mapFunction.entrySet().stream()
				.map(keyFun -> new TyperComposition(keyFun.getKey(), mapInput.get(keyFun.getKey()), keyFun.getValue()))
				.collect(Collectors.toMap(TyperComposition::getKey, TyperComposition::apply));
	}

	class TyperComposition{
		private final K key;
		private final V value;
		private final TypedFunction<V, ?> typedFunction;

		private TyperComposition(K key, V value, TypedFunction<V, ?> typedFunction) {
			super();
			this.key = key;
			this.value = value;
			this.typedFunction = typedFunction;
		}

		Try<TypedValue<?>> apply(){
			return typedFunction.apply(value);
		}

		K getKey() {
			return key;
		}
	}

}



