package impl.stages.annotations.validations.post;

import kernel.validation.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CsvPostValidation {
    Class<? extends Validator<Object>> validator();
    String[] params() default {};
}
