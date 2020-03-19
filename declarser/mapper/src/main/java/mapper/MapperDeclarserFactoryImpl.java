package mapper;

import kernel.Declarser;
import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.tryapi.Try;

final class MapperDeclarserFactoryImpl implements MapperDeclarserFactory{

    @Override
    public <I, O> Try<Declarser<I, String, Object, O>> declarserOf(
            final Class<I> fromClazz,
            final Class<I> toClazz) {
        ToMap<I, String, Object> toMap = null;
        ToTypedMap<String, Object> toTypedMap = null;
        Combinator<String> combinator = null;
        ToObject<String, O> toObject = null;
        return Try.success(Declarser.of(
                toMap,
                toTypedMap,
                combinator,
                toObject));
    }
}
