package csv.validation.utils;

import kernel.validations.Validator;
import kernel.validations.impl.fromstring.NonBlankStringValidator;
import kernel.validations.impl.fromstring.NonEmptyStringValidator;
import kernel.validations.impl.fromstring.StringPatternValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CsvValidationConst {

    private CsvValidationConst(){}

    public static final Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> prevalidatorClassMap;
    static {
        prevalidatorClassMap = new HashMap<>();
        prevalidatorClassMap.put(NonEmptyStringValidator.class, arr -> NonEmptyStringValidator.getInstance());
        prevalidatorClassMap.put(NonBlankStringValidator.class, arr -> NonBlankStringValidator.getInstance());
        prevalidatorClassMap.put(StringPatternValidator.class, arr -> StringPatternValidator.getInstance(arr[0]));
    }

}
