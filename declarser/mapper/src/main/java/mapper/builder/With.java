package mapper.builder;

import kernel.tryapi.Try;

import java.util.function.Function;

public interface With<I,O> {

    static <O, I> With<I, O> of(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName) {
        return WithImpl.of(mapperDeclarserBuilder, fieldName);
    }

    static <O, I> With<I, O> ofNothing(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder,
            final String fieldName) {
        return WithoutImpl.of(mapperDeclarserBuilder, fieldName);
    }

    As<I,O> as(
            final Function<I, Try<?>> function);
}
