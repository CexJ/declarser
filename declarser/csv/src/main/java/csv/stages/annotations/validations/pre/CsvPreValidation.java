package csv.stages.annotations.validations.pre;

import kernel.validations.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvPreValidation {
    Class<? extends Validator<String>> validator();
    String[] params() default {};
}
