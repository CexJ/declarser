package io.github.cexj.declarser.kernel.stages.stage01_tomap.impl;

import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.destructor.Destructor;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

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
