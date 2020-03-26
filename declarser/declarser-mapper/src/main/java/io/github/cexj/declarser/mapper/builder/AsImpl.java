package io.github.cexj.declarser.mapper.builder;

import io.github.cexj.declarser.kernel.Declarser;
import io.github.cexj.declarser.kernel.tryapi.Try;

final class AsImpl<I, O> implements As<I, O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;

    private AsImpl(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
    }

    static <O, I> AsImpl<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder) {
        return new AsImpl<>(mapperDeclarserBuilder);
    }

    @Override
    public With<I, O>  with(
            final String fieldName){
        return mapperDeclarserBuilder.with(fieldName);}

    @Override
    public Try<Declarser<I, String, Try<?>, O>> build() {
        return mapperDeclarserBuilder.build();
    }

}
