package examples.prevalidation.dynamic.samples;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.prevalidations.CsvPreValidation;
import kernel.validations.impl.fromstring.StringPatternValidator;


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
