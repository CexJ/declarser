package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.sample;

import csv.stages.annotations.fields.CsvColumn;

public class DataSample {

    @CsvColumn(0)
    private int age;

    @CsvColumn(1)
    private String name;

    private String address;


}
