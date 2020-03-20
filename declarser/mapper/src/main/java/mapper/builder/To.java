package mapper.builder;

import kernel.Declarser;
import kernel.tryapi.Try;

public class To<I,O> {

    private final MapperDeclarserBuilderImpl mapperDeclarserBuilder;

    private To(
            final MapperDeclarserBuilderImpl mapperDeclarserBuilder){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
    }

    static <I,O> To<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz) {
        return new To<>(MapperDeclarserBuilderImpl.of(fromClazz, toClazz));
    }

    public <O> Try<Declarser<I, String, Object, O>> build() {
        return mapperDeclarserBuilder.build();
    }
}
