import csv.CsvDeclarserFactory;
import org.junit.jupiter.api.Test;
import samples.DataSample;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnit5ExampleTest {

    @Test
    void justAnExample() {
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var declarser = declarserFactory.declarserOf(DataSample.class, ";");
        final var dataSample = declarser.getValue().parse("Sandro;10");
        assertEquals(dataSample.getValue().getName(),"Sandro");
    }
}
