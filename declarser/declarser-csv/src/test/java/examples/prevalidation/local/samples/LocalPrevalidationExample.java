package examples.prevalidation.local.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.kernel.validations.impl.fromstring.NonEmptyStringValidator;

public class LocalPrevalidationExample {

    @CsvColumn(0)
    @CsvPreValidation(NonEmptyStringValidator.class)
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
