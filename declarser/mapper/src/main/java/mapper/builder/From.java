package mapper.builder;

public class From<I> {

    private final Class<I> fromClazz;

    private From(
            final Class<I> fromClazz){
        this.fromClazz = fromClazz;
    }

    static <I> From<I> of(
            final Class<I> fromClazz) {
        return new From<>(fromClazz);
    }



    public <O> To<I,O> to(
            final Class<O> toClazz) {
        return To.of(fromClazz, toClazz);
    }
}
