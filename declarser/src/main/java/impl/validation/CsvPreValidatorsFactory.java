package impl.validation;

import impl.stages.annotations.validations.pre.CsvPreValidation;
import kernel.validation.Validator;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CsvPreValidatorsFactory {

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


    public Try<Validator<String>> function(final List<? extends CsvPreValidation> validatorAnns){
        final var tryValidators = validatorAnns.stream()
                .map(ann -> stringValidator(ann.validator(), ann.params()))
                .collect(Collectors.toList());

        final var hasErrors = tryValidators.stream().anyMatch(Try::isFailure);

        return hasErrors ? collectedErrors(tryValidators) :
                           composedFunction(tryValidators);
        }

    private Try<Validator<String>> composedFunction(final List<Try<Validator<String>>> tryValidators) {
        return Try.success(tryValidators.stream()
                    .map(Try::getValue)
                    .reduce(ok(), this::compose));
    }

    private Try<Validator<String>> collectedErrors(final List<Try<Validator<String>>> tryValidators) {
        return Try.fail(GroupedException.of(tryValidators.stream()
                        .filter(Try::isFailure)
                        .map(Try::getException)
                        .collect(Collectors.toList())));
    }

    public Try<Validator<String>> function(final CsvPreValidation validatorAnn){
       return stringValidator(validatorAnn.validator(), validatorAnn.params());
    }

    private Validator<String> ok() {
        return s -> Optional.empty();
    }

    private Validator<String> compose(final Validator<String> v1,final  Validator<String> v2) {
        return s -> v1.apply(s).isEmpty() ? v2.apply(s) : v1.apply(s);
    }

    private Try<Validator<String>> stringValidator(final Class<? extends Validator<String>> clazz, final String[] params){

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

    private Validator<String> fromMap(final Class<? extends Validator<String>> clazz, final String[] params) {
        return Optional.ofNullable(validatorClassMap.get(clazz)).map(v -> v.apply(params)).orElse(null);
    }


    private Validator<String> fromParamsConstructor(final Class<? extends Validator<String>> clazz, final String[] params) {
        try {
            return clazz.getConstructor(String[].class)
                    .newInstance((Object) params);
        } catch (Exception e) {
            return null;
        }
    }

    private Validator<String> fromNoArgsConstructor(final Class<? extends Validator<String>> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}

