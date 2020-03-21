package mapper.builder;

import kernel.Declarser;
import kernel.enums.SubsetType;
import kernel.tryapi.Try;

import java.util.HashMap;

final class ToImpl<I,O> implements To<I, O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;

    private ToImpl(
            final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
    }

    static <I,O> ToImpl<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz) {
        final var mapperDeclarserBuilder = MapperDeclarserBuilderImpl.of(fromClazz, toClazz, new HashMap<>(), SubsetType.NONE);
        return new ToImpl<>(mapperDeclarserBuilder);
    }

    @Override
    public As<I,O> withToFields(
            final SubsetType subsetType) {
        return mapperDeclarserBuilder.withToFields(subsetType);
    }

    @Override
    public With<I, O> with(
            final String fieldName) {
        return mapperDeclarserBuilder.with(fieldName);
    }

    @Override
    public Try<Declarser<I, String, Try<?>, O>> build() {
        return mapperDeclarserBuilder.build();
    }

}
