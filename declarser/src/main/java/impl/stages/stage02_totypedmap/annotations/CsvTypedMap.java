package impl.stages.stage02_totypedmap.annotations;

import utils.tryapi.Try;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvTypedMap {
    int key();
    Class<? extends Function<?, Try<?>>> function();
    String[] params() default {};
}
