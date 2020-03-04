package samples;

import csv.stages.annotations.fields.CsvColumn;
public class DataSample {

    @CsvColumn(0)
    private String name;

    @CsvColumn(1)
    private Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
