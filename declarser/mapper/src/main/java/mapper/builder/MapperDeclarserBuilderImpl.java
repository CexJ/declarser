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
import mapper.builder.exceptions.FieldNotFoundException;
import mapper.stages.stage_01.destructor.MapperDestructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MapperDeclarserBuilderImpl<I, O> implements MapperDeclarserBuilder{

    private final Class<I> fromClazz;
    private final Class<O> toClazz;
    private final Map<String, Function<I, Try<?>>> fieldFunctionMap;


    private MapperDeclarserBuilderImpl(
            final Class<I> fromClazz,
            final Class<O> toClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap){
        this.fromClazz = fromClazz;
        this.toClazz = toClazz;
        this.fieldFunctionMap = fieldFunctionMap;
    }

    static <I,O> MapperDeclarserBuilderImpl<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap) {
        return new MapperDeclarserBuilderImpl<>(fromClazz, toClazz, fieldFunctionMap);
    }

    Try<With<I, O>>  with(
            final String fieldName){
        return Stream.of(toClazz.getDeclaredFields())
                .map(Field::getName)
                .dropWhile(name -> ! name.equals(fieldName))
                .findFirst()
                .map(field -> Try.success(With.of(this, field)))
                .orElse(Try.fail(FieldNotFoundException.of(fieldName, toClazz)));
    }

    MapperDeclarserBuilderImpl<I,O> withAs(
            final String fieldName,
            final Function<I, Try<?>> function) {
        final var newFieldFunctionMap = new HashMap<>(fieldFunctionMap);
        newFieldFunctionMap.put(fieldName, function);
        return of(fromClazz, toClazz, newFieldFunctionMap);
    }

    Try<Declarser<I, String, Try<?>, O>> build() {
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

    private Map<String, String> mapField(Class<O> toClazz) {
        return Stream.of(toClazz.getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        Field::getName));
    }

    private Map<String, Function<Try<?>, Try<?>>> mapFunction(Class<I> fromClazz) {
        return Stream.of(fromClazz.getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> (Function<Try<?>, Try<?>>) t -> t));

    }

    private ToMap<I, String, Try<?>> stage01(Class<I> fromClazz) {
        return ToMap.of(
                i -> Optional.empty(),
                MapperDestructor.of(fromClazz, fieldFunctionMap));
    }
}
