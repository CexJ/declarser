package io.github.cexj.declarser.mapper.builder;

import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.mapper.builder.exceptions.FieldNotFoundException;

import java.util.function.Function;

final class WithoutImpl<I, O> implements With<I,O>{

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;
    private final String fieldName;

    private WithoutImpl(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
        this.fieldName = fieldName;
    }

    static <O, I> WithoutImpl<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName) {
        return new WithoutImpl<>(mapperDeclarserBuilder, fieldName);
    }

    public As<I,O> as(
            final Function<I, Try<?>> function){
        final Function<I, Try<?>> exceptionFunction = i -> Try.fail(FieldNotFoundException.of(fieldName, mapperDeclarserBuilder.getToClazz()));
        return mapperDeclarserBuilder.withAs(fieldName, exceptionFunction);
    }
}
