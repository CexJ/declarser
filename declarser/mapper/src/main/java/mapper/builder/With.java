package mapper.builder;

import kernel.tryapi.Try;

import java.util.function.Function;

public final class With<I, O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;
    private final String fieldName;

    private With(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
        this.fieldName = fieldName;
    }

    static <O, I> With<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName) {
        return new With<>(mapperDeclarserBuilder, fieldName);
    }

    public As<I,O> as(
            final Function<I, Try<?>> function){
        return As.of(mapperDeclarserBuilder.withAs(fieldName, function));
    }
}
