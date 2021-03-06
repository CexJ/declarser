package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.extractor;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldsExtractor;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CsvFieldsExtractor implements FieldsExtractor {

    private CsvFieldsExtractor() {}

    private static class InstanceHolder {
        private static final CsvFieldsExtractor instance = new CsvFieldsExtractor();
    }
    public static CsvFieldsExtractor getInstance(){
        return InstanceHolder.instance;
    }

    @Override
    public Set<Field> extract(
            final Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toSet());
    }

}
