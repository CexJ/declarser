package mapper.builder;

public interface From<I> {
    static <I> From<I> of(
            final Class<I> fromClazz) {
        return FromImpl.of(fromClazz);
    }

    <O> To<I,O> to(
            final Class<O> toClazz);
}
