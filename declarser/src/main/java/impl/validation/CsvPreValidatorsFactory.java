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
        return fromMap(clazz, params)              .or(
               fromParamsConstructor(clazz, params).or(
               fromNoArgsConstructor(clazz)        .or(
               Try.fail(new ClassNotFoundException()))));

    }

    private Try<Validator<String>> fromMap(final Class<? extends Validator<String>> clazz, final String[] params) {
        return Optional.ofNullable(Try.success(validatorClassMap.get(clazz)).map(v -> v.apply(params)))
                .orElse(Try.fail(new NullPointerException()));
    }


    private Try<Validator<String>> fromParamsConstructor(final Class<? extends Validator<String>> clazz, final String[] params) {
        return Try.go( () -> clazz.getConstructor(String[].class).newInstance((Object) params));
    }

    private Try<Validator<String>> fromNoArgsConstructor(final Class<? extends Validator<String>> clazz) {
        return Try.go( () ->clazz.getConstructor().newInstance());
    }

}
