package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvField;
import csv.stages.annotations.fields.CsvNode;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

final class CsvFieldTransformerImpl implements CsvFieldTransformer {

    private final static Class<?>[] EMPTY = new Class[]{};

    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;
    private final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;


    private CsvFieldTransformerImpl(
            final CsvDeclarserFactory csvDeclarserFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        this.csvDeclarserFactory = csvDeclarserFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
        this.autoFunctionClassMap = autoFunctionClassMap;
    }

    static CsvFieldTransformerImpl of(
            CsvDeclarserFactory csvDeclarserFactory,
            Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        return new CsvFieldTransformerImpl(
                csvDeclarserFactory,
                functionClassMap,
                autoFunctionClassMap);
    }


    @Override
    public Try<Function<String, Try<?>>> compute(Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvField.class)).map(this::fieldTransformer)              .orElse(
               Optional.ofNullable(field.getAnnotation(CsvNode.class)) .map(node -> nodeTransformer(field, node)).orElse(
               Optional.ofNullable(autoFunctionClassMap.get(autoType(field.getType()))).map(Try::success)        .orElse(
               Try.fail(MissingTransformerException.of(field)))));
    }

    private Class<?> autoType(Class<?> type) {
        return type.isArray() ? type.getComponentType() : type;
    }


    private Try<Function<String, Try<?>>> nodeTransformer(
            final Field field,
            final CsvNode csvNode) {
        final var cellSeparator = csvNode.value();
        return csvDeclarserFactory
                .declarserOf(field.getType(), cellSeparator)
                .map( dec -> dec::parse);
    }

    private Try<Function<String, Try<?>>> fieldTransformer(
            final CsvField csvField) {
        final var annFunction = csvField.value();
        final var annParams = csvField.params();

        return Optional.ofNullable(functionClassMap.get(annFunction))
                .map(f -> Try.success(f.apply(annParams)))
                .orElse(Try.go(() -> annFunction.getConstructor(String[].class).newInstance((Object) annParams)))
                .or(Try.go(() -> annFunction.getConstructor(EMPTY).newInstance()));

    }
}
