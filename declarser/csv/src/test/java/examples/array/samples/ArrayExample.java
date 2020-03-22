package examples.array.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvArray;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;

public class ArrayExample {

    @CsvColumn(0)
    @CsvArray
    private String[] aStringArray;

    public String[] getaStringArray() {
        return aStringArray;
    }
}
