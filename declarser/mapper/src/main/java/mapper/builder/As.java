package mapper.builder;

import kernel.Declarser;
import kernel.tryapi.Try;

public class As<I, O> {

    private final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder;

    private As(final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder){
        this.mapperDeclarserBuilder = mapperDeclarserBuilder;
    }

    static <O, I> As<I, O> of(final MapperDeclarserBuilderImpl<I,O> mapperDeclarserBuilder) {
        return new As<>(mapperDeclarserBuilder);
    }

    public Try<With<I, O>>  with(
            final String fieldName){
        return mapperDeclarserBuilder.with(fieldName);}

    public Try<Declarser<I, String, Try<?>, O>> build() {
        return mapperDeclarserBuilder.build();
    }

}
