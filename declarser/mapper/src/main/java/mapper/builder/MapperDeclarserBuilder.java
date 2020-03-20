package mapper.builder;

import mapper.builder.From;

public interface MapperDeclarserBuilder {

    static <I> From<I> from(final Class<I> fromClazz){
        return From.of(fromClazz);
    }
}
