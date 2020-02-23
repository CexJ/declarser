package main.exemple;

import impl.stages.annotations.fields.CsvField;
import impl.stages.annotations.type.CsvType;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.FromStringToInteger;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.FromStringToString;

@CsvType( cellSeparator = ",")
public class dataSample {

    @CsvField( key = 1,
               function = FromStringToString.class)
    private String name;

    @CsvField( key = 2,
            function = FromStringToInteger.class)
    private Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
