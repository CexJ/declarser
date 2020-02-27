package impl.validation;

import kernel.validation.Validator;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvValidatorsFactory<T> {

    private final Map<Class<? extends Validator<T>>,
            Function<String[], Validator<T>>> validatorClassMap;

    private CsvValidatorsFactory(final Map<Class<? extends Validator<T>>, Function<String[], Validator<T>>> validatorClassMap,
                                 final Map<Class<? extends Validator<T>>, Function<String[], Validator<T>>> customMap) {
        this(validatorClassMap);
        customMap.forEach(validatorClassMap::put);
    }
    private CsvValidatorsFactory(final Map<Class<? extends Validator<T>>, Function<String[], Validator<T>>> validatorClassMap) {
        this.validatorClassMap = new HashMap<>(validatorClassMap);
    }

    public static CsvValidatorsFactory<String> pre(final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
                                              final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap){
        return new CsvValidatorsFactory<>(validatorClassMap, customMap);
    }

    public static CsvValidatorsFactory<?> of(final Map<Class<? extends Validator<?>>, Function<String[], Validator<?>>> customMap){
        return new CsvValidatorsFactory(customMap);
    }

    public Try<Validator<T>> function(List<? extends ValidatorAnnImpl<T>> validatorAnns){
        final var tryValidators = validatorAnns.stream()
                .map(ann -> getPreValidator(ann.getClazz(), ann.getParams()))
                .collect(Collectors.toList());
        final var errors = tryValidators.stream()
                .filter(Try::isFailure)
                .map(Try::getException)
                .collect(Collectors.toList());
        if(errors.isEmpty()){
            return Try.success(tryValidators.stream()
                    .map(Try::getValue)
                    .reduce(ok(), this::compose));
        } else {
            return Try.fail(GroupedException.of(errors));
        }
    }

    public Try<Validator<T>> function(ValidatorAnnImpl<?> validatorAnn){
       return getPreValidator(validatorAnn.getClazz(), validatorAnn.getParams());
    }

    private Validator<T> ok() {
        return (T s) -> Optional.empty();
    }

    private Validator<T> compose(Validator<T> v1, Validator<T> v2) {
        return (T s) -> v1.apply(s).isEmpty() ? v2.apply(s) : v1.apply(s);
    }

    private Try<Validator<T>> getPreValidator(Class<? extends Validator<T>> clazz, String[] params){

        final var map = fromMap(clazz, params);
        if(map != null){
            return Try.success(map);
        }

        final var paramConstructor = fromParamsConstructor(clazz, params);
        if(paramConstructor != null){
            return Try.success(paramConstructor);
        }

        final var noArgsConstructor = fromNoArgsConstructor(clazz);
        if(noArgsConstructor != null){
            return Try.success(noArgsConstructor);
        }
        return Try.fail(new ClassNotFoundException());
    }

    private Validator<T> fromMap(Class<? extends Validator<T>> clazz, String[] params) {
        return Optional.ofNullable(validatorClassMap.get(clazz)).map(v -> v.apply(params)).orElse(null);
    }


    private Validator<T> fromParamsConstructor(Class<? extends Validator<T>> clazz, String[] params) {
        try {
            return clazz.getConstructor(String[].class)
                    .newInstance((Object) params);
        } catch (Exception e) {
            return null;
        }
    }

    private Validator<T> fromNoArgsConstructor(Class<? extends Validator<T>> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}

