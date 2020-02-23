package impl.stages.annotations.fields.validations.pre;

import utils.tryapi.Try;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvFieldPreValidation {
    Class<? extends Function<String, Optional<? extends Exception>>> validator();
    String[] params() default {};
}
