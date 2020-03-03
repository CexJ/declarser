package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.CsvDeclarserFactory;
import impl.stages.annotations.fields.CsvColumn;
import impl.stages.annotations.fields.CsvArray;
import impl.stages.annotations.fields.CsvField;
import impl.stages.annotations.fields.CsvNode;
import impl.validation.CsvPreValidatorsFactory;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.constants.Constants.EMPTY;

public final class CsvFunctionMapFactory {

    private final CsvPreValidatorsFactory preValidatorFactory;
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;
    private final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap = CsvFunctionMapFactoryConst.autoFunctionClassMap;
    private final CsvDeclarserFactory csvDeclarserFactory;

    private CsvFunctionMapFactory(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap) {
        this.csvDeclarserFactory = csvDeclarserFactory;
        this.preValidatorFactory = preValidatorFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
    }

    public static CsvFunctionMapFactory of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){
        return new CsvFunctionMapFactory(
                csvDeclarserFactory,
                preValidatorFactory,
                functionClassMap);
    }



    public Try<Map<Integer, Function<String, Try<?>>>> mapColumnToTransformer(
            final Class<?> clazz){
        final var partition = Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(CsvColumn.class) != null)
                .map(this::computeTransformer)
                .collect(Collectors.partitioningBy(Try::isSuccess));

        final var success = partition.get(true);
        final var failures = partition.get(false);

        return failures.isEmpty() ? collectSuccessMap(success)
                                  : collectFailureMap(failures);
    }

    private Try<Map<Integer, Function<String, Try<?>>>> collectFailureMap(
            final List<Try<CsvAnnotationImpl>> errors) {
        return Try.fail(GroupedException.of(errors.stream()
             .map(Try::getException)
             .collect(Collectors.toList())));
    }

    private Try<Map<Integer, Function<String, Try<?>>>> collectSuccessMap(
            final List<Try<CsvAnnotationImpl>> fields) {
        return Try.success(fields.stream()
                .map(Try::getValue)
                .collect(Collectors.toMap(CsvAnnotationImpl::getKey, CsvAnnotationImpl::getFunction)));
    }

    private Try<CsvAnnotationImpl> computeTransformer(
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
                CsvAnnotationImpl.of(csvColumn.value(), modifier.apply(tra)));
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
        final var annPrevalidators = csvField.csvPreValidations().validations();
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


final class CsvAnnotationImpl {
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvAnnotationImpl(
            final int key,
            final Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static CsvAnnotationImpl of(
            final Integer key,
            final Function<String, Try<?>> function) {
        return new CsvAnnotationImpl(key,function);
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

