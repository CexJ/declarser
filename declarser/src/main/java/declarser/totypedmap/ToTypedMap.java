package declarser.totypedmap;

import java.util.Map;

import declarser.totypedmap.typer.Typer;
import declarser.typedvalue.TypedValue;
import declarser.validator.Validator;
import declarser.validator.ValidatorMap;
import utils.tryapi.Try;

public class ToTypedMap<K,V> {
	
	private final Typer<K, V> typer;
	private final Validator<Map<K,V>> validator;
	private final ValidatorMap<K,TypedValue<?>> validatorMap;

	public ToTypedMap(Typer<K, V> typer, Validator<Map<K,V>> validator, ValidatorMap<K,TypedValue<?>> validatorMap) {
		super();
		this.typer = typer;
		this.validator = validator;
		this.validatorMap = validatorMap;
	}

	public Try<Map<K,TypedValue<?>>> apply(final Map<K,V> input){
		return validator.validate(input)
				.map(typer::type)
				.flatMap(validatorMap::validate);
	}

}
