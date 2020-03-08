package csv.stages.stage04_toobject.restructors;

import csv.stages.stage04_toobject.CsvFieldMapFactory;
import csv.stages.stage04_toobject.restructors.sample.SampleData;
import org.junit.jupiter.api.Test;

public class CsvFieldMapFactoryTest {

    @Test
    public void map_valid_calss_return_success(){
        CsvFieldMapFactory.mapFieldNameColumn(SampleData.class);
    }
}
