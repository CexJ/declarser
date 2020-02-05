package declarser.totypedmap;

import java.util.Map;

import declarser.totypedmap.typer.Typer;
import declarser.typedvalue.TypedValue;
import declarser.validator.ValidatorMap;
import utils.tryapi.Try;

public class ToTypedMap<K,V> {
	
	private final Typer<K, V> typer;
	private final ValidatorMap<K,TypedValue<?>> validatorMap;
	
	
	
	public ToTypedMap(Typer<K, V> typer, ValidatorMap<K,TypedValue<?>> validatorMap) {
		super();
		this.typer = typer;
		this.validatorMap = validatorMap;
		
	}

	public Try<Map<K,TypedValue<?>>> apply(final Map<K,V> map){
		return validatorMap.validate(typer.type(map));
	}

}
