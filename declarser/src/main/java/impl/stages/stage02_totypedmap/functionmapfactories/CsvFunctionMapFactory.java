package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.annotations.fields.CsvArrayField;
import impl.stages.annotations.fields.CsvField;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.constants.Constants.EMPTY;

public class CsvFunctionMapFactory {

    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap;

    private CsvFunctionMapFactory(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap) {
        this.functionClassMap = new HashMap<>(functionClassMap);
    }

    public static CsvFunctionMapFactory of(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap){
        return new CsvFunctionMapFactory(functionClassMap);
    }

    public Try<Map<Integer, Function<String, Try<?>>>> getMap(Class<?> clazz){
        final var partitionCsvField = getPartition(
                clazz,
                CsvField.class,
                ann -> CsvAnnotationImpl.ofField(ann, functionClassMap));

        final var partitionCsvArrayField = getPartition(
                clazz,
                CsvArrayField.class,
                ann -> CsvAnnotationImpl.ofArrayField(ann, functionClassMap));

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


    private Map<Boolean, List<Try<CsvAnnotationImpl>>> merge(List<Map<Boolean, List<Try<CsvAnnotationImpl>>>> partitions) {

        return partitions.stream()
                .map(Map::entrySet)
                .map(Set::stream)
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private <T extends Annotation> Map<Boolean, List<Try<CsvAnnotationImpl>>> getPartition(
            Class<?> clazz,
            Class<T> annClazz,
            Function<T, Try<CsvAnnotationImpl>> constructor){

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

