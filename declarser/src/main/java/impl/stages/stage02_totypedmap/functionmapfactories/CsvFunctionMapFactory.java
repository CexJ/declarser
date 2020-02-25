package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.annotations.fields.CsvArrayField;
import impl.stages.annotations.fields.CsvField;
import impl.validation.CsvValidatorsFactory;
import impl.validation.ValidatorAnnImpl;
import kernel.validation.Validator;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.constants.Constants.EMPTY;

public class CsvFunctionMapFactory {

    private final CsvValidatorsFactory<String> preValidatorFactory;
    private final CsvValidatorsFactory<?> postValidatorFactory;
    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;

    private CsvFunctionMapFactory(
            final CsvValidatorsFactory<String> preValidatorFactory,
            final CsvValidatorsFactory<?> postValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap) {
        this.preValidatorFactory = preValidatorFactory;
        this.postValidatorFactory = postValidatorFactory;
        this.functionClassMap = new HashMap<>(functionClassMap);
    }

    public static CsvFunctionMapFactory of(
            final CsvValidatorsFactory<String> preValidatorFactory,
            final CsvValidatorsFactory<?> postValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){
        return new CsvFunctionMapFactory(
                preValidatorFactory,
                postValidatorFactory,
                functionClassMap);
    }

    public Try<Map<Integer, Function<String, Try<?>>>> getMap(final Class<?> clazz){
        final var partitionCsvField = getPartition(
                clazz, CsvField.class,
                ann -> CsvAnnotationImpl.ofField(
                        ann,
                        preValidatorFactory,
                        postValidatorFactory,
                        functionClassMap));

        final var partitionCsvArrayField = getPartition(
                clazz, CsvArrayField.class,
                ann -> CsvAnnotationImpl.ofArrayField(
                        ann,
                        preValidatorFactory,
                        postValidatorFactory,
                        functionClassMap));

        final var partition = merge(List.of(partitionCsvField, partitionCsvArrayField));

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


    private Map<Boolean, List<Try<CsvAnnotationImpl>>> merge(final List<Map<Boolean, List<Try<CsvAnnotationImpl>>>> partitions) {

        return partitions.stream()
                .map(Map::entrySet)
                .map(Set::stream)
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private <T extends Annotation> Map<Boolean, List<Try<CsvAnnotationImpl>>> getPartition(
            final Class<?> clazz,
            final Class<T> annClazz,
            final Function<T, Try<CsvAnnotationImpl>> constructor){

        return Stream.of(clazz.getDeclaredFields())
                .map(f -> Optional.ofNullable(f.getAnnotation(annClazz)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(constructor)
                .collect(Collectors.partitioningBy(Try::isSuccess));
    }

}


class CsvAnnotationImpl {
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvAnnotationImpl(int key, Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static Try<CsvAnnotationImpl> ofField(
            final CsvField csvField,
            final CsvValidatorsFactory<String> preValidatorFactory,
            final CsvValidatorsFactory<?> postValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){


        final var preValidator = preValidatorFactory.function(Stream.of(csvField.csvPreValidations().preValidations())
                .map(pre -> ValidatorAnnImpl.pre(pre.validator(),pre.params()))
                .collect(Collectors.toList()));


        final var transformer = Optional.ofNullable(functionClassMap.get(csvField.function()))
                .map(f -> Try.success(f.apply(csvField.params())))
                .orElse(Try.go(() -> csvField.function().getConstructor(EMPTY).newInstance(csvField.params())));

        final Try<Function<String, Try<?>>> function =
                preValidator.flatMap( pre  ->
                transformer.map(  tras ->
                        s -> Try.success(s)
                                .continueIf(pre)
                                .map(tras)));

        return Optional.ofNullable(functionClassMap.get(csvField.function()))
                .map(f -> Try.success(f.apply(csvField.params())))
                .orElse(Try.go(() -> csvField.function().getConstructor(EMPTY).newInstance(csvField.params())))
                .map(f -> new CsvAnnotationImpl(csvField.key(),f));

    }

    static Try<CsvAnnotationImpl> ofArrayField(
            final CsvArrayField csvArrayFieldImpl,
            final CsvValidatorsFactory<String> preValidatorFactory,
            final CsvValidatorsFactory<?> postValidatorFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){

        return Optional.ofNullable(functionClassMap.get(csvArrayFieldImpl.function()))
                .map(f -> Try.success(f.apply( csvArrayFieldImpl.params())))
                .orElse(Try.go(() -> csvArrayFieldImpl.function().getConstructor(EMPTY).newInstance(csvArrayFieldImpl.params())))
                .map(f -> new CsvAnnotationImpl(csvArrayFieldImpl.key(),getArrayFunction(f, csvArrayFieldImpl.separator())));
    }

    private static Function<String, Try<?>> getArrayFunction(Function<String, Try<?>> function, String arraySeparator){
        return s -> Try.go(() -> combine(Arrays.stream(s.split(arraySeparator))
                .map(function)
                .collect(Collectors.toList()))
                .map(List::toArray));
    }

    private static Try<List<?>> combine(List<Try<?>> list){
        Map<Boolean, List<Try<?>>> partition = list.stream().collect(Collectors.partitioningBy(Try::isSuccess));
        if(partition.get(false).isEmpty()){
            return Try.success(list.stream().map(Try::getValue).collect(Collectors.toList()));
        } else {
            List<Exception> errors = partition.get(false).stream().map(Try::getException).collect(Collectors.toList());
            return Try.fail(GroupedException.of(errors));
        }
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

