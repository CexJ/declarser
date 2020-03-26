package io.github.cexj.declarser.csv.validation.utils.utils;

import io.github.cexj.declarser.kernel.validations.Validator;

import java.util.Optional;

public class NoArgsValidatorClass2 implements Validator<String> {

    @Override
    public Optional<? extends Exception> apply(String s) {
        return Constants.string1.equals(s)   ? Optional.empty() :
               ! Constants.string2.equals(s) ? Optional.empty() :
                                     Optional.of(Constants.exception);
    }
}
