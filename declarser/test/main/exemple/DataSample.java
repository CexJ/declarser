package main.exemple;

import impl.stages.annotations.fields.CsvColumn;
import impl.stages.annotations.fields.CsvField;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.FromStringToInteger;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.FromStringToString;

public class DataSample {

    @CsvColumn(0)
    @CsvField(FromStringToString.class)
    private String name;

    @CsvColumn(1)
    @CsvField(FromStringToInteger.class)
    private Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
