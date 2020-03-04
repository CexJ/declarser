package csv.stages.stage04_toobject;

import csv.stages.annotations.fields.CsvColumn;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class CsvFieldMapFactory {

    private CsvFieldMapFactory(){}

    public static <O> Map<String, Integer> mapFieldNameColumn(
            final Class<O> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(CsvColumn.class).value()));
    }
}
