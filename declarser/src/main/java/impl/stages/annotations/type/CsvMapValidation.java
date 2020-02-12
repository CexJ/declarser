package impl.stages.annotations.type;

import kernel.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CsvMapValidation {
    Class<? extends Validator<Map<Integer, String>>> mapVallidation();
}
