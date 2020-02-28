package impl.stages.annotations.fields;

import impl.stages.annotations.validations.pre.CsvPreValidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvNode {
    String cellSeparator();
    CsvPreValidations csvPreValidations() default @CsvPreValidations();
}
