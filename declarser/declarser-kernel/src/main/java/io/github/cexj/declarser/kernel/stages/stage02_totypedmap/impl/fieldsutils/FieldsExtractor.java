package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils;

import java.lang.reflect.Field;
import java.util.Set;

public interface FieldsExtractor {
    Set<Field> extract(Class<?> clazz);
}
