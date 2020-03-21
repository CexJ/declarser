package mapper.builder;

import kernel.Declarser;
import kernel.enums.FunctionType;
import kernel.enums.SubsetType;
import kernel.tryapi.Try;

import java.util.HashMap;

public final class To<I,O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;

    private To(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
    }

    static <I,O> To<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz) {
        final var mapperDeclarserBuilder = MapperDeclarserBuilderImpl.of(fromClazz, toClazz, new HashMap<>(), SubsetType.NONE);
        return new To<>(mapperDeclarserBuilder);
    }

    public As<I,O> asMap(
            final FunctionType functionType) {
        return As.of(mapperDeclarserBuilder.asMap(functionType));
    }

    public With<I, O> with(
            final String fieldName) {
        return mapperDeclarserBuilder.with(fieldName);
    }

    public Try<Declarser<I, String, Try<?>, O>> build() {
        return mapperDeclarserBuilder.build();
    }

}
