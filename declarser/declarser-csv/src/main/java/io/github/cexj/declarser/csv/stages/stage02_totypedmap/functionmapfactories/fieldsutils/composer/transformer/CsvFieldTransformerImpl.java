package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvField;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvNode;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

final class CsvFieldTransformerImpl implements CsvFieldTransformer {

    private final static Class<?>[] EMPTY = new Class[]{};

    private final Map<Class<? extends Parser<String,?>>, Function<String[], Parser<String,?>>> functionClassMap;
    private final Map<Class<?>, Parser<String,?>> autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;


    private CsvFieldTransformerImpl(
            final CsvDeclarserFactory csvDeclarserFactory,
            final Map<Class<? extends Parser<String,?>>, Function<String[], Parser<String,?>>> functionClassMap,
            final Map<Class<?>, Parser<String,?>> autoFunctionClassMap) {
        this.csvDeclarserFactory = csvDeclarserFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
        this.autoFunctionClassMap = autoFunctionClassMap;
    }

    static CsvFieldTransformerImpl of(
            CsvDeclarserFactory csvDeclarserFactory,
            Map<Class<? extends Parser<String,?>>, Function<String[], Parser<String,?>>> functionClassMap,
            Map<Class<?>, Parser<String,?>> autoFunctionClassMap) {
        return new CsvFieldTransformerImpl(
                csvDeclarserFactory,
                functionClassMap,
                autoFunctionClassMap);
    }


    @Override
    public Try<Parser<String,?>> compute(Field field) {
        return csvAnnotationCase(field)
               .orElse(csvNodeAnnotationCase(field)
               .orElse(autoCase(field)
               .orElse(Try.fail(MissingTransformerException.of(field)))));
    }

    private Optional<Try<Parser<String, ?>>> csvAnnotationCase(
            final Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvField.class)).map(this::fieldTransformer);
    }

    private Optional<Try<Parser<String, ?>>> csvNodeAnnotationCase(
            final Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvNode.class)).map(node -> nodeTransformer(field, node));
    }

    private Optional<Try<Parser<String, ?>>> autoCase(
            final Field field) {
        return Optional.ofNullable(autoFunctionClassMap.get(autoType(field.getType()))).map(Try::success);
    }

    private Class<?> autoType(
            final Class<?> type) {
        return type.isArray() ? type.getComponentType() : type;
    }


    private Try<Parser<String,?>> nodeTransformer(
            final Field field,
            final CsvNode csvNode) {
        final var cellSeparator = csvNode.value();
        return csvDeclarserFactory
                .declarserOf(field.getType(), cellSeparator)
                .map(dec -> dec);
    }

    private Try<Parser<String,?>> fieldTransformer(
            final CsvField csvField) {
        final var annFunction = csvField.value();
        final var annParams = csvField.params();

        return Optional.ofNullable(functionClassMap.get(annFunction))
                .map(f -> Try.<Parser<String,?>>success(f.apply(annParams)))
                .orElse(Try.call(() -> annFunction.getConstructor(String[].class).newInstance((Object) annParams)))
                .or(Try.call(() ->  annFunction.getConstructor(EMPTY).newInstance()));

    }
}
