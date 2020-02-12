package impl.stages.annotations.fields;

import utils.tryapi.Try;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiFunction;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvField {
    int key();
    Class<? extends BiFunction<Class<?>, String[], ? extends Function<?, Try<?>>>> function();
    String[] params() default {};
}
