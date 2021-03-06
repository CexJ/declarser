package io.github.cexj.declarser.csv.stages.annotations.prevalidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface CsvPreValidations {
    CsvPreValidation[] value();
}
