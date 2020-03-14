package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvField;
import csv.stages.annotations.fields.CsvNode;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

final class CsvFieldTransformerImpl implements CsvFieldTransformer {

    private final static Class<?>[] EMPTY = new Class[]{};

    private final CsvPreValidatorsFactory preValidatorFactory;
    private final CsvPreValidatorsExtractor csvPreValidatorsExtractor;
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;
    private final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;


    private CsvFieldTransformerImpl(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        this.csvDeclarserFactory = csvDeclarserFactory;
        this.preValidatorFactory = preValidatorFactory;
        this.csvPreValidatorsExtractor = csvPreValidatorsExtractor;
        this.functionClassMap = new HashMap<>(functionClassMap);
        this.autoFunctionClassMap = autoFunctionClassMap;
    }

    static CsvFieldTransformerImpl of(
            CsvDeclarserFactory csvDeclarserFactory,
            CsvPreValidatorsFactory preValidatorFactory,
            CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        return new CsvFieldTransformerImpl(
                csvDeclarserFactory,
                preValidatorFactory,
                csvPreValidatorsExtractor,
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
        final var cellSeparator = csvNode.cellSeparator();
        return csvDeclarserFactory.declarserOf(field.getClass(), cellSeparator)
                .map( dec -> dec::parse);
    }

    private Try<Function<String, Try<?>>> fieldTransformer(
            final CsvField csvField) {
        final var annPrevalidators = csvField.csvPreValidations().value();
        final var annFunction = csvField.value();
        final var annParams = csvField.params();

        final var preValidator = preValidatorFactory.function(csvPreValidatorsExtractor.extract(Arrays.asList(annPrevalidators)));

        final var transformer = Optional.ofNullable(functionClassMap.get(annFunction))
                .map(f -> Try.success(f.apply(annParams)))
                .orElse(Try.go(() -> annFunction.getConstructor(EMPTY).newInstance()));

        return preValidator.flatMap( pre ->
                transformer.map(     tra ->
                (String s) -> pre.apply(s).isEmpty() ? tra.apply(s) :
                Try.fail(pre.apply(s).get())));
    }
}
