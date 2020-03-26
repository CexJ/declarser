package io.github.cexj.declarser.mapper.builder;

public interface MapperDeclarserBuilder {

    static <I> From<I> from(
            final Class<I> fromClazz){
        return From.of(fromClazz);
    }
}
