package io.github.cexj.declarser.mapper.builder;

import io.github.cexj.declarser.kernel.Declarser;
import io.github.cexj.declarser.kernel.tryapi.Try;

public interface As<I, O> {

    static <O, I> As<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder) {
        return AsImpl.of(mapperDeclarserBuilder);
    }

    With<I, O> with(
            final String fieldName);

    Try<Declarser<I, String, Try<?>, O>> build();
}
