package declarser.totypedmap.typer;

import java.util.Map;

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
		return null;
	} 
	
}
