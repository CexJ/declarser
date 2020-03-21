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

    With<I, O>  with(
            final String fieldName){
        return Stream.of(toClazz.getDeclaredFields())
                .map(Field::getName)
                .dropWhile(name -> ! name.equals(fieldName))
                .findFirst()
                .map(field -> With.of(this, fieldName))
                .orElse(With.ofNothing(this, fieldName));
    }

    MapperDeclarserBuilderImpl<I,O> withAs(
            final String fieldName,
            final Function<I, Try<?>> function) {
        final var newFieldFunctionMap = new HashMap<>(fieldFunctionMap);
        newFieldFunctionMap.put(fieldName, function);
        return of(fromClazz, toClazz, newFieldFunctionMap);
    }

    Try<Declarser<I, String, Try<?>, O>> build() {
        final var toMap = stage01();
        final var toTypedMap = stage02();
        final var combinator = stage03();
        final var toObject = stage04();
        return Try.success(Declarser.of(
                toMap,
                toTypedMap,
                combinator,
                toObject));
    }

    private ToMap<I, String, Try<?>> stage01() {
        return ToMap.of(
                i -> Optional.empty(),
                MapperDestructor.of(fromClazz, fieldFunctionMap));
    }

    private ToTypedMap<String, Try<?>> stage02() {
        return ToTypedMap.of(
                mapFunction(),
                SubsetType.NONE,
                ParallelizationStrategyEnum.SEQUENTIAL);
    }

    private Combinator<String> stage03() {
        return Combinator.noException(ParallelizationStrategyEnum.SEQUENTIAL);
    }

    private ToObject<String, O> stage04() {
        return ToObject.of(
                o -> Optional.empty(),
                Restructor.reflection(
                        toClazz,
                        mapField(),
                        SubsetType.NONE,
                        SubsetType.NONE).getValue());
    }

    private Map<String, String> mapField() {
        return Stream.of(toClazz.getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        Field::getName));
    }

    private Map<String, Function<Try<?>, Try<?>>> mapFunction() {
        return Stream.of(Stream.of(toClazz.getDeclaredFields()).map(Field::getName),
                fieldFunctionMap.keySet().stream())
                .flatMap(i -> i)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        fieldName -> (Function<Try<?>, Try<?>>) t -> t));
    }


    Class<O> getToClazz() {
        return toClazz;
    }

}
