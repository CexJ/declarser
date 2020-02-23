package impl.stages.annotations.validations.pre;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvPreValidation {
    Class<? extends Function<String, Optional<? extends Exception>>> validator();
    String[] params() default {};
}
