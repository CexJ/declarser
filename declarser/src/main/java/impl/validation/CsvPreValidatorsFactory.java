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

public class CsvPreValidatorsFactory {

    private final Map<Class<? extends Validator<String>>,
            Function<String[], Validator<String>>> validatorClassMap;

    private CsvPreValidatorsFactory(final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
                                    final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap) {
        this(validatorClassMap);
        customMap.forEach(validatorClassMap::put);
    }
    private CsvPreValidatorsFactory(final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap) {
        this.validatorClassMap = new HashMap<>(validatorClassMap);
    }

    public static CsvPreValidatorsFactory of(final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
                                             final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap){
        return new CsvPreValidatorsFactory(validatorClassMap, customMap);
    }


    public Try<Validator<String>> function(List<? extends ValidatorAnnImpl<String>> validatorAnns){
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

    public Try<Validator<String>> function(ValidatorAnnImpl<String> validatorAnn){
       return getPreValidator(validatorAnn.getClazz(), validatorAnn.getParams());
    }

    private Validator<String> ok() {
        return s -> Optional.empty();
    }

    private Validator<String> compose(Validator<String> v1, Validator<String> v2) {
        return s -> v1.apply(s).isEmpty() ? v2.apply(s) : v1.apply(s);
    }

    private Try<Validator<String>> getPreValidator(Class<? extends Validator<String>> clazz, String[] params){

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

    private Validator<String> fromMap(Class<? extends Validator<String>> clazz, String[] params) {
        return Optional.ofNullable(validatorClassMap.get(clazz)).map(v -> v.apply(params)).orElse(null);
    }


    private Validator<String> fromParamsConstructor(Class<? extends Validator<String>> clazz, String[] params) {
        try {
            return clazz.getConstructor(String[].class)
                    .newInstance((Object) params);
        } catch (Exception e) {
            return null;
        }
    }

    private Validator<String> fromNoArgsConstructor(Class<? extends Validator<String>> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}

