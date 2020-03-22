package examples.custom.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvField;
import examples.custom.parsers.DynamicIntegerGreaterThanParser;

public class CustomDynamicExample {

    @CsvColumn(0)
    @CsvField(DynamicIntegerGreaterThanParser.class)
    private Integer anInteger;

    public Integer getAnInteger() {
        return anInteger;
    }
}
