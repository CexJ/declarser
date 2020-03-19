package kernel.stages.stage04_toobject.impl;

import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.util.Map;

public interface ToObject<K, O> {

    static <K,O> ToObject<K,O> of(
            final Validator<O> outputValidator,
            final Restructor<K,O> restructor){
        return ToObjectImpl.of(
                outputValidator,
                restructor);
    }

    Try<O> gluing(final Map<K, ?> map);
}
