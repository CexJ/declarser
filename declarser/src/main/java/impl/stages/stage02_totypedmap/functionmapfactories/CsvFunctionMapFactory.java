package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.annotations.fields.CsvArrayField;
import impl.stages.annotations.fields.CsvField;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDate;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToZonedDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigDecimal;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigInteger;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.*;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.FromStringToString;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.constants.Constants.EMPTY;

public class CsvFunctionMapFactory {

    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap = new HashMap<>();
    {
        functionClassMap.put(FromStringToLocalDate.class, arr -> FromStringToLocalDate.getInstance(arr[0]));
        functionClassMap.put(FromStringToLocalDateTime.class, arr -> FromStringToLocalDateTime.getInstance(arr[0]));
        functionClassMap.put(FromStringToZonedDateTime.class, arr -> FromStringToZonedDateTime.getInstance(arr[0]));
        functionClassMap.put(FromStringToBigDecimal.class, arr -> FromStringToBigDecimal.getInstance());
        functionClassMap.put(FromStringToBigInteger.class, arr -> FromStringToBigInteger.getInstance());
        functionClassMap.put(FromStringToBoolean.class, arr -> FromStringToBoolean.getInstance());
        functionClassMap.put(FromStringToCharacter.class, arr -> FromStringToCharacter.getInstance());
        functionClassMap.put(FromStringToDouble.class, arr -> FromStringToDouble.getInstance());
        functionClassMap.put(FromStringToFloat.class, arr -> FromStringToFloat.getInstance());
        functionClassMap.put(FromStringToInteger.class, arr -> FromStringToInteger.getInstance());
        functionClassMap.put(FromStringToLong.class, arr -> FromStringToLong.getInstance());
        functionClassMap.put(FromStringToShort.class, arr -> FromStringToShort.getInstance());
        functionClassMap.put(FromStringToString.class, arr -> FromStringToString.getInstance());
    }


    private CsvFunctionMapFactory(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customMap) {
        customMap.entrySet().forEach(kv -> functionClassMap.put(kv.getKey(),kv.getValue()));
    }

    public static CsvFunctionMapFactory of(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customMap){
        return new CsvFunctionMapFactory(customMap);
    }

    public Try<Map<Integer, Function<String, Try<?>>>> getMap(Class<?> clazz){
        final var partitionCsvField = getPartition(CsvField.class,
                ann -> CsvAnnotationImpl.ofField(ann, functionClassMap));
        final var partitionCsvArrayField = getPartition(CsvArrayField.class,
                ann -> CsvAnnotationImpl.ofArrayField(ann, functionClassMap));
        final var partition = merge(partitionCsvField, partitionCsvArrayField);

        final var fields = partition.get(true);
        final var errors = partition.get(false);

        if(errors.isEmpty())
            return Try.success(fields.stream()
                    .map(Try::getValue)
                    .collect(Collectors.toMap(CsvAnnotationImpl::getKey, CsvAnnotationImpl::getFunction)));
        else
            return (Try<Map<Integer, Function<String, Try<?>>>>) Try.fail(GroupedException.of(errors.stream()
                    .map(Try::getException)
                    .collect(Collectors.toList())));
    }


    private Map<Boolean, List<Try<CsvAnnotationImpl>>> merge(Map<Boolean, List<Try<CsvAnnotationImpl>>>... partitions) {

        return Stream.of(partitions)
                .map(Map::entrySet)
                .map(Set::stream)
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private <T extends Annotation> Map<Boolean, List<Try<CsvAnnotationImpl>>> getPartition(Class<T> clazz,
                                                                                           Function<T, Try<CsvAnnotationImpl>> constructor){
        return Stream.of(clazz.getDeclaredFields())
                .map(f -> Optional.ofNullable(f.getAnnotation(clazz)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ann -> constructor.apply(ann))
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

    static Try<CsvAnnotationImpl> ofField(final CsvField csvField,
                                          final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){

        return Optional.ofNullable(functionClassMap.get(csvField.function()))
                .map(f -> Try.success(f.apply(csvField.params())))
                .orElse(Try.go(() -> csvField.function().getConstructor(EMPTY).newInstance(csvField.params())))
                .map(f -> new CsvAnnotationImpl(csvField.key(),f));

    }

    static Try<CsvAnnotationImpl> ofArrayField(final CsvArrayField csvArrayFieldImpl,
                                               final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){

        return Optional.ofNullable(functionClassMap.get(csvArrayFieldImpl.function()))
                .map(f -> Try.success(getArrayFunction(f.apply(csvArrayFieldImpl.params()), csvArrayFieldImpl.separator())))
                .orElse(Try.go(() -> csvArrayFieldImpl.function().getConstructor(EMPTY).newInstance(csvArrayFieldImpl.params())))
                .map(f -> new CsvAnnotationImpl(csvArrayFieldImpl.key(),f));
    }

    private static Function<String, Try<?>> getArrayFunction(Function<String, Try<?>> function, String arraySeparator){
        return s -> Try.go(() -> combine(Arrays.stream(s.split(arraySeparator))
                .map(function)
                .collect(Collectors.toList()))
                .map(l -> l.toArray()));
    }

    private static Try<List<?>> combine(List<Try<?>> list){
        Map<Boolean, List<Try<?>>> partition = list.stream().collect(Collectors.partitioningBy(el -> el.isSuccess()));
        if(partition.get(false).isEmpty()){
            return Try.success(list.stream().map(Try::getValue).collect(Collectors.toList()));
        } else {
            List<Exception> errors = partition.get(false).stream().map(Try::getException).collect(Collectors.toList());
            return (Try<List<?>>) Try.fail(GroupedException.of(errors));
        }
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

