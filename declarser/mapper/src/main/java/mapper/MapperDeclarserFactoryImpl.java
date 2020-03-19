package mapper;

import kernel.Declarser;
import kernel.enums.ParallelizationStrategyEnum;
import kernel.enums.SubsetType;
import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.tryapi.Try;
import mapper.stages.stage_01.destructor.MapperDestructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class MapperDeclarserFactoryImpl implements MapperDeclarserFactory{



    @Override
    public <I, O> Try<Declarser<I, String, Object, O>> declarserOf(
            final Class<I> fromClazz,
            final Class<I> toClazz) {
        final var toMap = stage01(fromClazz);
        final var toTypedMap = ToTypedMap.of(
                mapFunction(fromClazz),
                SubsetType.BIJECTIVE,
                ParallelizationStrategyEnum.SEQUENTIAL);
        final var combinator = Combinator.<String>noException(ParallelizationStrategyEnum.SEQUENTIAL);
        ToObject<String, O> toObject = null;
        return Try.success(Declarser.of(
                toMap,
                toTypedMap,
                combinator,
                toObject));
    }

    private <I> Map<String, Function<Object, Try<?>>> mapFunction(Class<I> fromClazz) {
        return Stream.of(fromClazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> (Function<Object, Try<?>>) Try::success));

    }

    private <I> ToMap<I, String, Object> stage01(Class<I> fromClazz) {
        return ToMap.of(
                i -> Optional.empty(),
                MapperDestructor.of(fromClazz));
    }
}
