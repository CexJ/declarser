package examples.array.samples;

import csv.stages.annotations.fields.CsvArray;
import csv.stages.annotations.fields.CsvColumn;

public class ArrayExample {

    @CsvColumn(0)
    @CsvArray
    private String[] aStringArray;

    public String[] getaStringArray() {
        return aStringArray;
    }
}
