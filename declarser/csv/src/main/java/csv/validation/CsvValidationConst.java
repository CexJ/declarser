package csv.validation;

import kernel.validations.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CsvValidationConst {

    private CsvValidationConst(){}

    public static final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>>
            prevalidatorClassMap = new HashMap<>();

}
