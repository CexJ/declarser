package csv.stages.stage04_toobject.restructors.sample;

import csv.stages.annotations.fields.CsvColumn;

public class InvalidSample {

    @CsvColumn(0)
    private String name;

    @CsvColumn(0)
    private Integer age;
}
