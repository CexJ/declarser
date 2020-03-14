package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvArray;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.fields.CsvField;
import csv.stages.annotations.fields.CsvNode;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import csv.validation.utils.CsvPreValidatorsFactory;
import csv.validation.utils.CsvPreValidatorsExtractor;
import kernel.exceptions.GroupedException;
import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import kernel.stages.stage02_totypedmap.impl.impl.Transformer;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CsvFieldComposer implements FieldComposer<Integer, String> {


    public final static Class<?>[] EMPTY = new Class[]{};

    private final CsvPreValidatorsFactory preValidatorFactory;
    private final CsvPreValidatorsExtractor csvPreValidatorsExtractor;
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;
    private final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;


    private CsvFieldComposer(
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

    public static CsvFieldComposer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        return new CsvFieldComposer(
                csvDeclarserFactory,
                preValidatorFactory,
                csvPreValidatorsExtractor,
                functionClassMap,
                autoFunctionClassMap);
    }


    public Try<Transformer<Integer,String>> compute(
            final Field field) {
        final var modifier = computeModifier(field);

        final var transformer = computeTransformer(field);

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        return transformer.flatMap( tra ->
               modifier.map(        mod ->
                       Transformer.of(csvColumn.value(), mod.apply(tra))));
    }

    private Try<Function<String, Try<?>>> computeTransformer(Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvField.class)).map(this::fieldTransformer)              .orElse(
               Optional.ofNullable(field.getAnnotation(CsvNode.class)) .map(node -> nodeTransformer(field, node)).orElse(
               Optional.ofNullable(autoFunctionClassMap.get(autoType(field.getType()))).map(Try::success)        .orElse(
               Try.fail(MissingTransformerException.of(field)))));
    }

    private Try<UnaryOperator<Function<String, Try<?>>>> computeModifier(Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvArray.class))
                .map(arr -> field.getType().isArray() ? Try.success(getArrayFunction(arr.value()))
                                                      : Try.<UnaryOperator<Function<String, Try<?>>>>fail(MissingArrayException.of(field)))
                .orElse(                                Try.success(UnaryOperator.identity()));
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

    private UnaryOperator<Function<String, Try<?>>> getArrayFunction(
            final String arraySeparator){
        return (Function<String, Try<?>> fun) ->
                s -> combine(Arrays.stream(s.split(arraySeparator))
                                .map(fun)
                                .collect(Collectors.toList()))
                    .map(List::toArray);
    }

    private Try<List<?>> combine(
            final List<Try<?>> list){
        final var partition = list.stream()
                .collect(Collectors.partitioningBy(Try::isSuccess));

        final var success =  partition.get(true);
        final var failures =  partition.get(false);


        return failures.isEmpty() ? collectSuccessList(success)
                                  : collectFailureList(failures);
    }

    private Try<List<?>> collectSuccessList(
            final List<Try<?>> success) {
        return Try.success(success.stream()
                .map(Try::getValue)
                .collect(Collectors.toList()));
    }

    private Try<List<?>> collectFailureList(
            final List<Try<?>> failures) {
        return Try.fail(GroupedException.of(failures.stream()
                .map(Try::getException)
                .collect(Collectors.toList())));
    }

}
