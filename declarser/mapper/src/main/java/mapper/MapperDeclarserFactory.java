package mapper;

import kernel.Declarser;
import kernel.tryapi.Try;
public interface MapperDeclarserFactory {

    <I,O> Try<Declarser<I, String, Object, O>> declarserOf(
            final Class<I> fromClazz,
            final Class<O> toClazz);
}
