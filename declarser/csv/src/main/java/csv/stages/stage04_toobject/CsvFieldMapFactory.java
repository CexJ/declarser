package csv.stages.stage04_toobject;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage04_toobject.exceptions.RepeatedCsvColumnIndexException;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class CsvFieldMapFactory {

    private CsvFieldMapFactory(){}

    public static <O> Try<Map<String, Integer>> mapFieldNameColumn(
            final Class<O> clazz) {
        final var group = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.groupingBy(field -> field.getAnnotation(CsvColumn.class).value()));
        final var repetitions = group.entrySet().stream()
                .filter(es -> es.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return repetitions.isEmpty() ? Try.success(group.entrySet().stream()
                                    .collect(Collectors.toMap(es -> es.getValue().get(0).getName(), Map.Entry::getKey)))
                              : Try.fail(RepeatedCsvColumnIndexException.of(repetitions, clazz));
    }
}
