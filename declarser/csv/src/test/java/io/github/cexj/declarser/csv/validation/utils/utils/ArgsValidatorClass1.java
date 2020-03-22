package io.github.cexj.declarser.csv.validation.utils.utils;

import io.github.cexj.declarser.kernel.validations.Validator;

import java.util.Optional;

import static io.github.cexj.declarser.csv.validation.utils.utils.Constants.exception;

public class ArgsValidatorClass1 implements Validator<String> {

    private String string1;
    private String string2;

    public ArgsValidatorClass1(String... args){
        string1 = args[0];
        string2 = args[1];
    }

    @Override
    public Optional<? extends Exception> apply(String s) {
        return string1.equals(s) ? Optional.empty() :
               string2.equals(s) ? Optional.empty() :
                                   Optional.of(exception);
    }
}
