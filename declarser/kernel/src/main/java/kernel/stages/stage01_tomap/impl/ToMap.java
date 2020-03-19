package kernel.stages.stage01_tomap.impl;

import kernel.stages.stage01_tomap.impl.destructor.Destructor;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.util.Map;

public interface ToMap<I, K, V> {

    static <I,K,V> ToMap<I,K,V> of(
            final Validator<I> inputValidator,
            final Destructor<I, K, V> destructor){
        return ToMapImpl.of(
                inputValidator,
                destructor);
    }

    Try<Map<K, V>> mapping(final I input);
}
