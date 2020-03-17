package csv.validation.utils.factory;

import kernel.validations.Validator;
import kernel.exceptions.GroupedException;
import kernel.tryapi.Try;
import kernel.validations.prevalidations.PreValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

final class CsvPreValidatorsFactoryImpl implements CsvPreValidatorsFactory {

    private final Map<Class<? extends Validator<String>>,
            Function<String[], Validator<String>>> validatorClassMap;

    private CsvPreValidatorsFactoryImpl(
            final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
            final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap) {
        this(validatorClassMap);
        this.validatorClassMap.putAll(customMap);
    }
    private CsvPreValidatorsFactoryImpl(
            final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap) {
        this.validatorClassMap = new HashMap<>(validatorClassMap);
    }

    static CsvPreValidatorsFactoryImpl of(
            final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
            final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap){
        return new CsvPreValidatorsFactoryImpl(validatorClassMap, customMap);
    }

    @Override
    public Try<Validator<String>> function(
            final List<? extends PreValidator<String>> validatorAnns){
        final var tryValidators = validatorAnns.stream()
                .map(ann -> stringValidator(ann.getClazz(), ann.getParams()))
                .collect(Collectors.toList());

        final var hasErrors = tryValidators.stream().anyMatch(Try::isFailure);

        return hasErrors ? collectedErrors(tryValidators) :
                           composedFunction(tryValidators);
    }

    private Try<Validator<String>> composedFunction(
            final List<Try<Validator<String>>> tryValidators) {
        return Try.success(tryValidators.stream()
                    .map(Try::getValue)
                    .reduce(ok(), this::compose));
    }

    private Try<Validator<String>> collectedErrors(
            final List<Try<Validator<String>>> tryValidators) {
        return Try.fail(GroupedException.of(tryValidators.stream()
                        .filter(Try::isFailure)
                        .map(Try::getException)
                        .collect(Collectors.toList())));
    }

    private Validator<String> ok() {
        return s -> Optional.empty();
    }

    private Validator<String> compose(
            final Validator<String> v1,
            final  Validator<String> v2) {
        return s -> v1.apply(s).isEmpty() ? v2.apply(s) : v1.apply(s);
    }

    private Try<Validator<String>> stringValidator(
            final Class<? extends Validator<String>> clazz,
            final String[] params){
        return fromMap(clazz, params)              .or(
               fromParamsConstructor(clazz, params).or(
               fromNoArgsConstructor(clazz)        .or(
               Try.fail(new ClassNotFoundException()))));

    }

    private Try<Validator<String>> fromMap(
            final Class<? extends Validator<String>> clazz,
            final String[] params) {
        return Optional.ofNullable(Try.success(validatorClassMap.get(clazz)).map(v -> v.apply(params)))
                .orElse(Try.fail(new NullPointerException()));
    }


    private Try<Validator<String>> fromParamsConstructor(
            final Class<? extends Validator<String>> clazz,
            final String[] params) {
        return Try.go( () -> clazz.getConstructor(String[].class).newInstance((Object) params));
    }

    private Try<Validator<String>> fromNoArgsConstructor(
            final Class<? extends Validator<String>> clazz) {
        return Try.go( () ->clazz.getConstructor().newInstance());
    }

}

