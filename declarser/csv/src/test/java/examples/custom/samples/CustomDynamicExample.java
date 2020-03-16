package examples.custom.samples;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.fields.CsvField;
import examples.custom.parsers.DynamicIntegerGreaterThanParser;
import examples.custom.parsers.IntegerGreaterThanParser;

public class CustomDynamicExample {

    @CsvColumn(0)
    @CsvField(DynamicIntegerGreaterThanParser.class)
    private Integer anInteger;

    public Integer getAnInteger() {
        return anInteger;
    }
}
