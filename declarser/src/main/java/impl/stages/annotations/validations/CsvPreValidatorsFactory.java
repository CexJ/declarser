package impl.stages.annotations.validations;

import impl.stages.annotations.validations.pre.CsvPreValidations;
import impl.stages.stage03_combinator.combinators.NoExceptionCombinator;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvPreValidatorsFactory {

    private final Map<Class<? extends Function<String, Optional<? extends Exception>>>,
            Function<String[], Function<String, Optional<? extends Exception>>>> validatorClassMap = null;

    private CsvPreValidatorsFactory(Map<Class<? extends Function<String, Optional<? extends Exception>>>,
            Function<String[], Function<String, Optional<? extends Exception>>>> customMap) {
        customMap.entrySet().forEach(kv -> validatorClassMap.put(kv.getKey(),kv.getValue()));
    }

    public static CsvPreValidatorsFactory of(Map<Class<? extends Function<String, Optional<? extends Exception>>>,
            Function<String[], Function<String, Optional<? extends Exception>>>> customMap){
        return new CsvPreValidatorsFactory(customMap);
    }

    public Try<Function<String, Optional<? extends Exception>>> function(CsvPreValidations csvPreValidations){
        final var tryValidators = Stream.of(csvPreValidations.preValidations())
                .map(ann -> getPreValidator(ann.validator(), ann.params()))
                .collect(Collectors.toList());
        final var errors = tryValidators.stream()
                .filter(Try::isFailure)
                .map(Try::getException)
                .collect(Collectors.toList());
        if(errors.isEmpty()){
            return Try.success(tryValidators.stream()
                    .map(Try::getValue)
                    .reduce(ok(),
                            (f1, f2) -> compose(f1,f2)));
        } else {
            return Try.fail(GroupedException.of(errors));
        }
    }

    private Function<String, Optional<? extends Exception>> ok() {
        return (String s) -> Optional.empty();
    }

    private Function<String, Optional<? extends Exception>> compose(Function<String, Optional<? extends Exception>> v1, Function<String, Optional<? extends Exception>> v2) {
        return (String s) -> v1.apply(s).isEmpty() ? v2.apply(s) : v1.apply(s);
    }

    private Try<Function<String, Optional<? extends Exception>>> getPreValidator(Class<? extends Function<String, Optional<? extends Exception>>> clazz, String[] params){

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

    private Function<String, Optional<? extends Exception>> fromMap(Class<? extends Function<String, Optional<? extends Exception>>> clazz, String[] params) {
        return Optional.ofNullable(validatorClassMap.get(clazz)).map(v -> v.apply(params)).orElse(null);
    }


    private Function<String, Optional<? extends Exception>> fromParamsConstructor(Class<? extends Function<String, Optional<? extends Exception>>> clazz, String[] params) {
        try {
            return clazz.getConstructor(String[].class)
                    .newInstance(params);
        } catch (Exception e) {
            return null;
        }
    }

    private Function<String, Optional<? extends Exception>> fromNoArgsConstructor(Class<? extends Function<String, Optional<? extends Exception>>> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}

