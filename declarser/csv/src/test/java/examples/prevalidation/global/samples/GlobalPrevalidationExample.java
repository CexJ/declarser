package examples.prevalidation.global.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.StringPatternValidator;

@CsvPreValidation(
        value = StringPatternValidator.class,
        params = {"[a-z;]*"})
public class GlobalPrevalidationExample {

    @CsvColumn(0)
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
