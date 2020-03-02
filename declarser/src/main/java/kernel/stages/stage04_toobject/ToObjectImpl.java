package kernel.stages.stage04_toobject;

import java.util.Map;
import kernel.stages.stage04_toobject.restructor.Restructor;
import kernel.validation.Validator;
import utils.exceptions.OutputGluingException;
import utils.tryapi.Try;

public class ToObjectImpl<K,O> implements ToObject<K, O> {

	private final Validator<O> outputValidator;
	private final Restructor<K,O> restructor;
	
	private ToObjectImpl(final Validator<O> outputValidator, final Restructor<K,O> restructor) {
		super();
		this.outputValidator = outputValidator;
		this.restructor = restructor;
	}
	
	public static <K,O> ToObjectImpl<K,O> of(final Validator<O> outputValidator, final Restructor<K,O> restructor){
		return new ToObjectImpl<>(outputValidator, restructor);
	}

	@Override
	public Try<O> gluing(final Map<K, ?> map){
		return restructor.restruct(map)
				.continueIf(outputValidator)
				.enrichException(ex -> OutputGluingException.of(map, ex));
	}
}
