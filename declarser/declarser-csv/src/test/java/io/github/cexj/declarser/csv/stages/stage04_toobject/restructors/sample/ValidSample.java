package io.github.cexj.declarser.csv.stages.stage04_toobject.restructors.sample;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;

public class ValidSample {

    @CsvColumn(0)
    private String name;

    @CsvColumn(1)
    private Integer age;
}
