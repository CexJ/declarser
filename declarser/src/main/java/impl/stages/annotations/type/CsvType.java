package impl.stages.annotations.type;

import impl.stages.annotations.validations.post.CsvPostValidations;
import impl.stages.annotations.validations.pre.CsvPreValidations;
import kernel.conf.ParallelizationStrategyEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static kernel.conf.ParallelizationStrategyEnum.SEQUENTIAL;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CsvType {
    String CELL_SEPARATOR_DEFAULT = ";";
    String cellSeparator() default CELL_SEPARATOR_DEFAULT;
    CsvPreValidations csvPreValidations() default @CsvPreValidations();
    CsvPostValidations csvPostValidations() default @CsvPostValidations();

}
