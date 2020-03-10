package csv.validation.utils.utils;

import kernel.validations.Validator;

import java.util.Optional;

import static csv.validation.utils.utils.Constants.*;

public class NoArgsValidatorClass2 implements Validator<String> {

    @Override
    public Optional<? extends Exception> apply(String s) {
        return string1.equals(s)   ? Optional.empty() :
               ! string2.equals(s) ? Optional.empty() :
                                     Optional.of(exception);
    }
}
