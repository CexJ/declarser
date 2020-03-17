package examples.prevalidation.global.samples;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.prevalidations.CsvPreValidation;
import kernel.validations.impl.fromstring.NonEmptyStringValidator;
import kernel.validations.impl.fromstring.StringPatternValidator;

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
