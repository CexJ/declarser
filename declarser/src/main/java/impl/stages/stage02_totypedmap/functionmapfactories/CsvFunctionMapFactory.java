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



    public Try<Map<Integer, Function<String, Try<?>>>> mapColumnToTransformer(final Class<?> clazz){
        final var partition = Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(CsvColumn.class) != null)
                .map(this::computeTransformer)
                .collect(Collectors.partitioningBy(Try::isSuccess));


        final var fields = partition.get(true);
        final var errors = partition.get(false);

        if(errors.isEmpty())
            return Try.success(fields.stream()
                    .map(Try::getValue)
                    .collect(Collectors.toMap(CsvAnnotationImpl::getKey, CsvAnnotationImpl::getFunction)));
        else
            return Try.fail(GroupedException.of(errors.stream()
                    .map(Try::getException)
                    .collect(Collectors.toList())));
    }

    private Try<CsvAnnotationImpl> computeTransformer(final Field field) {

        final var csvArrayField = field.getAnnotation(CsvArray.class);
        final UnaryOperator<Function<String, Try<?>>> modifier =
                csvArrayField != null ? fun -> getArrayFunction(fun, csvArrayField.separator())
                                      : UnaryOperator.identity();

        final var csvField = field.getAnnotation(CsvField.class);
        final var csvNode = field.getAnnotation(CsvNode.class);
        final Try<Function<String, Try<?>>> function =
                csvField != null ? fieldTransformer(csvField) :
                 csvNode != null ? nodeTransformer(field, csvNode)
                                 : Try.fail(new NullPointerException());

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        return function
                .map(fun -> CsvAnnotationImpl.of(csvColumn.value(), modifier.apply(fun)));
    }

    private Try<Function<String, Try<?>>> nodeTransformer(final Field field, final CsvNode csvNode) {
        final var cellSeparator = csvNode.cellSeparator();
        return csvDeclarserFactory.declarserOf(field.getClass(), cellSeparator)
                .map( dec -> dec::parse);
    }

    private Try<Function<String, Try<?>>> fieldTransformer(final CsvField csvField) {
        final var annPrevalidators = csvField.csvPreValidations().validations();
        final var annFunction = csvField.value();
        final var annParams = csvField.params();

        final var preValidator = preValidatorFactory.function(Arrays.asList(annPrevalidators));

        final var transformer = Optional.ofNullable(functionClassMap.get(annFunction))
                                    .map(f -> Try.success(f.apply(annParams)))
                                    .orElse(Try.go(() -> annFunction.getConstructor(EMPTY).newInstance()));

        return preValidator.flatMap( pre  ->
                transformer.map(     tra ->
                        (String s) -> pre.apply(s).isEmpty() ? tra.apply(s)
                                                             : Try.fail(pre.apply(s).get())));
    }

    private Function<String, Try<?>> getArrayFunction(final Function<String, Try<?>> function, final String arraySeparator){
        return s -> Try.go(() -> combine(Arrays.stream(s.split(arraySeparator))
                .map(function)
                .collect(Collectors.toList()))
                .map(List::toArray));
    }

    private Try<List<?>> combine(final List<Try<?>> list){
        final var partition = list.stream().collect(Collectors.partitioningBy(Try::isSuccess));
        if(partition.get(false).isEmpty()){
            return Try.success(list.stream().map(Try::getValue).collect(Collectors.toList()));
        } else {
            final var errors = partition.get(false).stream().map(Try::getException).collect(Collectors.toList());
            return Try.fail(GroupedException.of(errors));
        }
    }

}


final class CsvAnnotationImpl {
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvAnnotationImpl(final int key, final Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static CsvAnnotationImpl of(final Integer key, final Function<String, Try<?>> function) {
        return new CsvAnnotationImpl(key,function);
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

