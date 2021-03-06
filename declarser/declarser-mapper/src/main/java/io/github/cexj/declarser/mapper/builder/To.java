package io.github.cexj.declarser.mapper.builder;

import io.github.cexj.declarser.kernel.Declarser;
import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.tryapi.Try;


public interface To<I, O> {

    static <I,O> To<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz) {
        return ToImpl.of(fromClazz, toClazz);
    }

    As<I, O> withTargetFields(
            final SubsetType functionType);

    With<I, O> with(
            final String fieldName);

    Try<Declarser<I, String, Try<?>, O>> build();
}
