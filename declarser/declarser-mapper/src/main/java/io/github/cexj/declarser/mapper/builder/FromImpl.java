package io.github.cexj.declarser.mapper.builder;

final class FromImpl<I> implements From<I> {

    private final Class<I> fromClazz;

    private FromImpl(
            final Class<I> fromClazz){
        this.fromClazz = fromClazz;
    }

    static <I> FromImpl<I> of(
            final Class<I> fromClazz) {
        return new FromImpl<>(fromClazz);
    }

    @Override
    public <O> To<I,O> to(
            final Class<O> toClazz) {
        return To.of(fromClazz, toClazz);
    }
}
