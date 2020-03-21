package mapper.builder;

import kernel.tryapi.Try;

import java.util.function.Function;

final class WithImpl<I, O> implements With<I,O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;
    private final String fieldName;

    private WithImpl(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
        this.fieldName = fieldName;
    }

    static <O, I> WithImpl<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName) {
        return new WithImpl<>(mapperDeclarserBuilder, fieldName);
    }

    public As<I,O> as(
            final Function<I, Try<?>> function){
        return As.of(mapperDeclarserBuilder.withAs(fieldName, function));
    }
}
