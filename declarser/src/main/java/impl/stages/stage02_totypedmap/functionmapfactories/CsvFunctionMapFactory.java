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
import kernel.Declarser;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.util.*;
import java.util.function.BiFunction;
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
        final var partitionCsvField = Stream.of(clazz.getDeclaredFields())
                .map(f -> Optional.ofNullable(f.getAnnotation(CsvField.class)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ann -> CsvFieldImpl.of(ann, functionClassMap))
                .collect(Collectors.partitioningBy(Try::isSuccess));
        final var csvFieldErrors = partitionCsvField.get(false);

        final var partitionCsvArrayField = Stream.of(clazz.getDeclaredFields())
                .map(f -> Optional.ofNullable(f.getAnnotation(CsvArrayField.class)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ann -> CsvArrayFieldImpl.of(ann, functionClassMap))
                .collect(Collectors.partitioningBy(Try::isSuccess));
        final var csvArrayFieldErrors = partitionCsvArrayField.get(false);

        if(csvFieldErrors.isEmpty())
            return Try.success(partitionCsvField.get(true).stream()
                    .map(Try::getValue)
                    .collect(Collectors.toMap(CsvFieldImpl::getKey,CsvFieldImpl::getFunction)));
        else
            return (Try<Map<Integer, Function<String, Try<?>>>>) Try.fail(GroupedException.of(csvFieldErrors.stream()
                    .map(Try::getException)
                    .collect(Collectors.toList())));
    }

}


class CsvFieldImpl{
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvFieldImpl(int key, Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static Try<CsvFieldImpl> of(final CsvField csvField,
                           final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){

        return Optional.ofNullable(functionClassMap.get(csvField.function()))
                .map(f -> Try.success(f.apply(csvField.params())))
                .orElse(Try.go(() -> csvField.function().getConstructor(EMPTY).newInstance(csvField.params())))
                .map(f -> new CsvFieldImpl(csvField.key(),f));

    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
    }
}

class CsvArrayFieldImpl{
    private final Integer key;
    private final Function<String, Try<?>> function;

    private CsvArrayFieldImpl(int key, Function<String, Try<?>> function) {
        this.key = key;
        this.function = function;
    }

    static Try<CsvArrayFieldImpl> of(final CsvArrayField csvArrayFieldImpl,
                                     final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){

        return Optional.ofNullable(functionClassMap.get(csvArrayFieldImpl.function()))
                .map(f -> Try.success(getArrayFunction(f.apply(csvArrayFieldImpl.params()), csvArrayFieldImpl.separator())))
                .orElse(Try.go(() -> csvArrayFieldImpl.function().getConstructor(EMPTY).newInstance(csvArrayFieldImpl.params())))
                .map(f -> new CsvArrayFieldImpl(csvArrayFieldImpl.key(),f));
    }

    public int getKey() {
        return key;
    }

    public Function<String, Try<?>> getFunction() {
        return function;
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
}
