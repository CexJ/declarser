import csv.impl.CsvDeclarserFactoryImp;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnit5ExampleTest {

    @Test
    void justAnExample() {
        final var declarserFactory = CsvDeclarserFactoryImp.defaultFactory();
        final var declarser = declarserFactory.declarserOf(DataSample.class, ";");
        final var dataSample = declarser.getValue().parse("Sandro;10");
        assertEquals(dataSample.getValue().getName(),"Sandro");
        assertEquals(dataSample.getValue().getAge(),10);
    }
}

