package examples.custom.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvField;
import examples.custom.parsers.IntegerGreaterThanParser;

public class CustomSimpleExample {

    @CsvColumn(0)
    @CsvField(
            value = IntegerGreaterThanParser.class,
            params = {"0"})
    private Integer anInteger;

    public Integer getAnInteger() {
        return anInteger;
    }
}
