package io.github.cexj.declarser.csv.validation.utils.utils;

import io.github.cexj.declarser.kernel.validations.Validator;

import java.util.Optional;

public class NoValidConstructorValidatorClass1 implements Validator<String> {

    public NoValidConstructorValidatorClass1(int i){
    }

    @Override
    public Optional<? extends Exception> apply(String s) {
        return Optional.empty();
    }
}
