package io.github.cexj.declarser.mapper.builder;

import io.github.cexj.declarser.kernel.Declarser;
import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.exceptions.SubsetTypeException;
import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.ToMap;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import io.github.cexj.declarser.kernel.stages.stage03_combinator.Combinator;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.ToObject;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.restructor.Restructor;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.mapper.stages.stage_01.destructor.MapperDestructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum.SEQUENTIAL;
import static io.github.cexj.declarser.kernel.enums.SubsetType.CONTAINED;
import static io.github.cexj.declarser.kernel.enums.SubsetType.NONE;

final class MapperDeclarserBuilderImpl<I, O> implements MapperDeclarserBuilder{

    private final Class<I> fromClazz;
    private final Class<O> toClazz;
    private final Map<String, Function<I, Try<?>>> fieldFunctionMap;
    private final SubsetType subsetType;

    private final Set<String> toFieldNames;
    private final Set<String> fromClassFieldNames;
    private final Set<String> fromCustomFieldNames;
    private final Set<String> fromFieldNames;


    private MapperDeclarserBuilderImpl(
            final Class<I> fromClazz,
            final Class<O> toClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap,
            final SubsetType subsetType){
        this.fromClazz = fromClazz;
        this.toClazz = toClazz;
        this.fieldFunctionMap = fieldFunctionMap;
        this.subsetType = subsetType;

        this.toFieldNames = Stream.of(toClazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        this.fromClassFieldNames = Stream.of(fromClazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        this.fromCustomFieldNames = fieldFunctionMap.keySet();
        this.fromFieldNames = Stream.of(
                fromClassFieldNames.stream(),
                fromCustomFieldNames.stream()).flatMap(i -> i)
                .collect(Collectors.toSet());
    }

    static <I,O> MapperDeclarserBuilderImpl<I,O> of(
            final Class<I> fromClazz,
            final Class<O> toClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap,
            final SubsetType subsetType) {
        return new MapperDeclarserBuilderImpl<>(fromClazz, toClazz, fieldFunctionMap, subsetType);
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

    As<I,O> withAs(
            final String fieldName,
            final Function<I, Try<?>> function) {
        final var newFieldFunctionMap = new HashMap<>(fieldFunctionMap);
        newFieldFunctionMap.put(fieldName, function);
        return As.of(of(fromClazz, toClazz, newFieldFunctionMap, subsetType));
    }

    As<I,O> withToFields(
            final SubsetType subsetType) {
        return As.of(of(fromClazz, toClazz, fieldFunctionMap, subsetType));
    }

    Try<Declarser<I, String, Try<?>, O>> build() {
        final var check = 
                subTypeStaticCheck().or(this::
                customNameStaticCheck);
        final var toMap = stage01();
        final var toTypedMap = stage02();
        final var combinator = stage03();
        final var tryToObject = stage04();
        return check.isEmpty() ? tryToObject.map( toObject -> Declarser.of(toMap, toTypedMap, combinator, toObject)) :
                                 Try.fail(check.get());
    }

    private Optional<SubsetTypeException> customNameStaticCheck() {
        return CONTAINED.validation(fromCustomFieldNames, toFieldNames);
    }

    private Optional<SubsetTypeException> subTypeStaticCheck() {
        return subsetType.validation(toFieldNames, fromFieldNames);
    }

    private ToMap<I, String, Try<?>> stage01() {
        return ToMap.of(
                i -> Optional.empty(),
                MapperDestructor.of(fromClazz, fieldFunctionMap));
    }

    private ToTypedMap<String, Try<?>> stage02() {
        return ToTypedMap.of(mapFunction(), NONE, SEQUENTIAL);
    }

    private Combinator<String> stage03() {
        return Combinator.noException(SEQUENTIAL);
    }

    private Try<ToObject<String, O>> stage04() {
        return Restructor.reflection(toClazz, mapField(), NONE, NONE)
                .map(restructor -> ToObject.of(o -> Optional.empty(),restructor));
    }

    private Map<String, String> mapField() {
        return Stream.of(toClazz.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Field::getName));
    }

    private Map<String, Function<Try<?>, Try<?>>> mapFunction() {
        return Stream.of(Stream.of(
                toClazz.getDeclaredFields()).map(Field::getName),
                fieldFunctionMap.keySet().stream()).flatMap(i -> i)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), fieldName -> (Function<Try<?>, Try<?>>) t -> t));
    }


    Class<O> getToClazz() {
        return toClazz;
    }

}
