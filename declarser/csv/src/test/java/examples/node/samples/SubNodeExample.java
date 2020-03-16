package examples.node.samples;

import csv.stages.annotations.fields.CsvColumn;

public class SubNodeExample {

    @CsvColumn(0)
    private Integer firstInteger;

    @CsvColumn(1)
    private Integer secondInteger;

    public Integer getFirstInteger() {
        return firstInteger;
    }

    public Integer getSecondInteger() {
        return secondInteger;
    }
}
