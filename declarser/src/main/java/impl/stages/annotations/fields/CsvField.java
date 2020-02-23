package impl.stages.annotations.fields;

import impl.stages.annotations.validations.post.CsvPostValidations;
import impl.stages.annotations.validations.pre.CsvPreValidations;
import utils.tryapi.Try;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvField {
    int key();
    CsvPreValidations csvPreValidations() default @CsvPreValidations();
    Class<? extends Function<String, Try<?>>> function();
    CsvPostValidations csvPostValidations() default @CsvPostValidations();
    String[] params() default {};
}
