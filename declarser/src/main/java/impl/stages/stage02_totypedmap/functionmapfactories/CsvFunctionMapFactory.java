package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.annotations.fields.CsvColumn;
import impl.stages.annotations.fields.CsvArray;
import impl.stages.annotations.fields.CsvField;
import impl.stages.annotations.fields.CsvNode;
import impl.validation.CsvValidatorsFactory;
import impl.validation.ValidatorAnnImpl;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.constants.Constants.EMPTY;

public class CsvFunctionMapFactory {

    private final CsvValidatorsFactory<String> preValidatorFactory;
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;

    private CsvFunctionMapFactory(
            final CsvValidatorsFactory<String> preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap) {
        this.preValidatorFactory = preValidatorFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
    }

    public static CsvFunctionMapFactory of(
            final CsvValidatorsFactory<String> preValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){
        return new CsvFunctionMapFactory(
                preValidatorFactory,
                functionClassMap);
    }



    public Try<Map<Integer, Function<String, Try<?>>>> getMap(final Class<?> clazz){
        final Map<Boolean, List<Try<CsvAnnotationImpl>>> partition = Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(CsvColumn.class) != null)
                .map( f -> computeFunction(f))
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

    private Try<CsvAnnotationImpl> computeFunction(Field field) {

        final var csvArrayField = field.getAnnotation(CsvArray.class);
        final UnaryOperator<Function<String, Try<?>>> modifier =
                csvArrayField != null ? fun -> getArrayFunction(fun, csvArrayField.separator())
                                      : UnaryOperator.identity();

        final var csvField = field.getAnnotation(CsvField.class);
        final var csvNode = field.getAnnotation(CsvNode.class);
        final Try<Function<String, Try<?>>> function =
                csvField != null ? fieldTransformer(csvField) :
                 csvNode != null ? nodeTransformer(csvNode) :
                                   Try.fail(new NullPointerException());

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        return function.map(fun -> CsvAnnotationImpl.of(csvColumn.key(), modifier.apply(fun)));
    }

    private Try<Function<String, Try<?>>> nodeTransformer(CsvNode csvNode) {

        return null;
    }

    private Try<Function<String, Try<?>>> fieldTransformer(CsvField csvField) {

        final var annPrevalidators = csvField.csvPreValidations().validations();
        final var annFunction = csvField.function();
        final var annParams = csvField.params();

        final var preValidator = preValidatorFactory.function(Stream.of(annPrevalidators)
                .map(pre -> ValidatorAnnImpl.pre(pre.validator(),pre.params()))
                .collect(Collectors.toList()));

        final var transformer = Optional.ofNullable(functionClassMap.get(annFunction))
                .map(f -> Try.success(f.apply(annParams)))
                .orElse(Try.go(() -> annFunction.getConstructor(EMPTY).newInstance()));

        return preValidator.flatMap( pre  ->
                transformer.map(  tras ->
                        s -> Try.success(s)
                                .continueIf(pre)
                                .map(tras)));
    }

    private Function<String, Try<?>> getArrayFunction(Function<String, Try<?>> function, String arraySeparator){
        return s -> Try.go(() -> combine(Arrays.stream(s.split(arraySeparator))
                .map(function)
                .collect(Collectors.toList()))
                .map(List::toArray));
    }

    private Try<List<?>> combine(List<Try<?>> list){
        Map<Boolean, List<Try<?>>> partition = list.stream().collect(Collectors.partitioningBy(Try::isSuccess));
        if(partition.get(false).isEmpty()){
            return Try.success(list.stream().map(Try::getValue).collect(Collectors.toList()));
        } else {
            List<Exception> errors = partition.get(false).stream().map(Try::getException).collect(Collectors.toList());
            return Try.fail(GroupedException.of(errors));
        }
    }


    private Map<Boolean, List<Try<CsvAnnotationImpl>>> merge(final List<Map<Boolean, List<Try<CsvAnnotationImpl>>>> partitions) {

        return partitions.stream()
                .map(Map::entrySet)
                .map(Set::stream)
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}


class CsvAnnotationImpl {
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvAnnotationImpl(int key, Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static CsvAnnotationImpl of(final Integer key, Function<String, Try<?>> function) {
        return new CsvAnnotationImpl(key,function);
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

