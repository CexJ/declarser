package csv.stages.annotations.prevalidations;

import kernel.validations.Validator;

import java.lang.annotation.*;

@Repeatable(CsvPreValidations.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface CsvPreValidation {
    Class<? extends Validator<String>> value();
    String[] params() default {};
}
