package examples.subset.samples;

import csv.stages.annotations.fields.CsvColumn;

public class SubsetExample {

    @CsvColumn(0)
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
