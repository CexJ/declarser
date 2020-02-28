package main.exemple;

import impl.stages.annotations.fields.CsvColumn;
import impl.stages.annotations.fields.CsvField;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.IntegerParser;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.StringParser;

public class DataSample {

    @CsvColumn(0)
    @CsvField(StringParser.class)
    private String name;

    @CsvColumn(1)
    @CsvField(IntegerParser.class)
    private Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
