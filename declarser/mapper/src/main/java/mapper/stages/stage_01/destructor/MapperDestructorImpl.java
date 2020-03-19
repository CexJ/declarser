package mapper.stages.stage_01.destructor;

import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MapperDestructorImpl<I> implements  MapperDestructor<I> {

    private final Set<Field> fields;

    private MapperDestructorImpl(Class<I> fromClazz) {
        this.fields = Stream.of(fromClazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toSet());
    }

    static <I> MapperDestructor<I> of(Class<I> fromClazz) {
        return new MapperDestructorImpl<>(fromClazz);
    }

    @Override
    public Try<Map<String, Object>> destruct(I input) {
        return Try.success(fields.stream().collect(Collectors.toMap(
                Field::getName,
                field -> Try.go(() -> field.get(input)).getValue())));

    }
}
