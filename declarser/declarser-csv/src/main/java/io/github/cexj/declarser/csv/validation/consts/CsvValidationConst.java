package io.github.cexj.declarser.csv.validation.consts;

import io.github.cexj.declarser.kernel.validations.Validator;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.NonBlankStringValidator;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.NonEmptyStringValidator;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.StringPatternValidator;

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
