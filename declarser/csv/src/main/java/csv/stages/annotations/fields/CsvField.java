package csv.stages.annotations.fields;

import csv.stages.annotations.validations.pre.CsvPreValidations;
import kernel.tryapi.Try;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvField {
    Class<? extends Function<String, Try<?>>> value();
    String[] params() default {};
}
