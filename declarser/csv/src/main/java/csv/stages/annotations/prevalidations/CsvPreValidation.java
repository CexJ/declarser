package csv.stages.annotations.prevalidations;

import kernel.validations.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvPreValidation {
    Class<? extends Validator<String>> value();
    String[] params() default {};
}
