package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvArray;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.fields.CsvField;
import csv.stages.annotations.fields.CsvNode;
import csv.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactoryConst;
import csv.validation.CsvPreValidatorsFactory;
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
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;
    private final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap = CsvFunctionMapFactoryConst.autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;

    private CsvFieldComposer(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap) {
        this.csvDeclarserFactory = csvDeclarserFactory;
        this.preValidatorFactory = preValidatorFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
    }

    public static CsvFieldComposer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){
        return new CsvFieldComposer(
                csvDeclarserFactory,
                preValidatorFactory,
                functionClassMap);
    }


    public Try<Transformer<Integer,String>> computeTransformer(
            final Field field) {
        final var modifier = Optional.ofNullable(field.getAnnotation(CsvArray.class))
                .map(arr -> (UnaryOperator<Function<String, Try<?>>>) (Function<String, Try<?>> fun) -> getArrayFunction(fun, arr.separator()))
                .orElse(UnaryOperator.identity());

        final var transformer =
                Optional.ofNullable(field.getAnnotation(CsvField.class)).map(this::fieldTransformer)              .orElse(
                        Optional.ofNullable(field.getAnnotation(CsvNode.class)) .map(node -> nodeTransformer(field, node)).orElse(
                                Optional.ofNullable(autoFunctionClassMap.get(field.getType())).map(Try::success)                  .orElse(
                                        Try.fail(new NullPointerException()))));
        final var csvColumn = field.getAnnotation(CsvColumn.class);

        return transformer.map(tra ->
                Transformer.of(csvColumn.value(), modifier.apply(tra)));
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

        final var preValidator = preValidatorFactory.function(Arrays.asList(annPrevalidators));

        final var transformer = Optional.ofNullable(functionClassMap.get(annFunction))
                .map(f -> Try.success(f.apply(annParams)))
                .orElse(Try.go(() -> annFunction.getConstructor(EMPTY).newInstance()));

        return preValidator.flatMap( pre ->
                transformer.map(     tra -> (String s) ->
                        pre.apply(s).isEmpty() ? tra.apply(s)
                                : Try.fail(pre.apply(s).get())));
    }

    private Function<String, Try<?>> getArrayFunction(
            final Function<String, Try<?>> function,
            final String arraySeparator){
        return s -> Try.go(() -> combine(Arrays.stream(s.split(arraySeparator))
                .map(function)
                .collect(Collectors.toList()))
                .map(List::toArray));
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

    private Try<List<?>> collectFailureList(
            final List<Try<?>> failures) {
        return Try.fail(GroupedException.of(failures.stream()
                .map(Try::getException)
                .collect(Collectors.toList())));
    }

    private Try<List<?>> collectSuccessList(
            final List<Try<?>> success) {
        return Try.success(success.stream()
                .map(Try::getValue)
                .collect(Collectors.toList()));
    }
}
