package mapper.stages.stage_01.destructor;

import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MapperDestructorImpl<I> implements  MapperDestructor<I> {

    private final Set<Field> fields;
    private final Map<String, Function<I, Try<?>>> fieldFunctionMap;

    private MapperDestructorImpl(
            final Class<I> fromClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap) {
        this.fields = Stream.of(fromClazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toSet());
        this.fieldFunctionMap = fieldFunctionMap;
    }

    static <I> MapperDestructor<I> of(
            final Class<I> fromClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap) {
        return new MapperDestructorImpl<>(fromClazz, fieldFunctionMap);
    }

    @Override
    public Try<Map<String, Try<?>>> destruct(I input) {
        final var mapResult = new HashMap<>(baseMap(input));
        mapResult.putAll(customMap(input));
        return Try.success(mapResult);
    }

    private Map<String, Try<?>> baseMap(I input) {
        return fields.stream().collect(Collectors.toMap(
                Field::getName,
                field -> Try.go(() -> field.get(input))));
    }

    private Map<String, Try<?>> customMap(I input) {
        return fieldFunctionMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        kv -> kv.getValue().apply(input)));

    }
}
