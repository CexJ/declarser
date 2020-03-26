package examples.prevalidation.dynamic.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.StringPatternValidator;


public class DynamicPrevalidationExample {

    @CsvColumn(0)
    @CsvPreValidation(StringPatternValidator.class)
    private String firstString;

    @CsvColumn(1)
    private String secondString;

    public String getFirstString() {
        return firstString;
    }

    public String getSecondString() {
        return secondString;
    }
}
