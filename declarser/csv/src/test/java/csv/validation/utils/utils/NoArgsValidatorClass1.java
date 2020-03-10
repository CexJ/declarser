package csv.validation.utils.utils;

import kernel.validations.Validator;

import java.util.Optional;

import static csv.validation.utils.utils.Constants.string1;
import static csv.validation.utils.utils.Constants.string2;
import static csv.validation.utils.utils.Constants.exception;

public class NoArgsValidatorClass1 implements Validator<String> {

    @Override
    public Optional<? extends Exception> apply(String s) {
        return string1.equals(s) ? Optional.empty() :
               string2.equals(s) ? Optional.empty() :
                                   Optional.of(exception);
    }
}
