package examples.prevalidation.local.samples;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.prevalidations.CsvPreValidation;
import csv.stages.annotations.prevalidations.CsvPreValidations;
import kernel.validations.impl.fromstring.NonEmptyStringValidator;

public class LocalPrevalidationExample {

    @CsvColumn(0)
    @CsvPreValidations({
            @CsvPreValidation(NonEmptyStringValidator.class)
    })
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
