package mapper.builder;

import kernel.Declarser;
import kernel.enums.ParallelizationStrategyEnum;
import kernel.enums.SubsetType;
import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.tryapi.Try;
import mapper.stages.stage_01.destructor.MapperDestructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MapperDeclarserBuilderImpl<I, O>   {

    private final Class<I> fromClazz;
    private final Class<O> toClazz;


    private MapperDeclarserBuilderImpl(
            final Class<I> fromClazz,
            final Class<O> toClazz){
        this.fromClazz = fromClazz;
        this.toClazz = toClazz;
    }

    static <I,O> MapperDeclarserBuilderImpl of(
            final Class<I> fromClazz,
            final Class<O> toClazz) {
        return new MapperDeclarserBuilderImpl(fromClazz, toClazz);
    }


    Try<Declarser<I, String, Object, O>> build() {
        final var toMap = stage01(fromClazz);
        final var toTypedMap = ToTypedMap.of(
                mapFunction(fromClazz),
                SubsetType.BIJECTIVE,
                ParallelizationStrategyEnum.SEQUENTIAL);
        final var combinator = Combinator.<String>noException(ParallelizationStrategyEnum.SEQUENTIAL);
        final var toObject = ToObject.of(
                o -> Optional.empty(),
                Restructor.reflection(
                        toClazz,
                        mapField(toClazz),
                        SubsetType.BIJECTIVE,
                        SubsetType.BIJECTIVE).getValue());
        return Try.success(Declarser.of(
                toMap,
                toTypedMap,
                combinator,
                toObject));
    }

    private <O> Map<String, String> mapField(Class<O> toClazz) {
        return Stream.of(toClazz.getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        Field::getName));
    }

    private <I> Map<String, Function<Object, Try<?>>> mapFunction(Class<I> fromClazz) {
        return Stream.of(fromClazz.getDeclaredFields())
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
