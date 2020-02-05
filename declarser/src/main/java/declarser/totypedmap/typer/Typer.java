package declarser.totypedmap.typer;

import java.util.Map;

import declarser.typedvalue.TypedValue;
import utils.tryapi.Try;

public interface Typer<K,V> {

	Map<K, Try<TypedValue<?>>> type(Map<K,V> mapInput); 
	
}
