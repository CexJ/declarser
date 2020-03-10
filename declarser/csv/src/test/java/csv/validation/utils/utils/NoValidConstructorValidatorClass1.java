package csv.validation.utils.utils;

import kernel.validations.Validator;

import java.util.Optional;

import static csv.validation.utils.utils.Constants.exception;

public class NoValidConstructorValidatorClass1 implements Validator<String> {

    public NoValidConstructorValidatorClass1(int i){
    }

    @Override
    public Optional<? extends Exception> apply(String s) {
        return Optional.empty();
    }
}
