import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvColumn;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnit5ExampleTest {

    @Test
    void justAnExample() {
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var declarser = declarserFactory.declarserOf(DataSample.class, ";");
        final var dataSample = declarser.getValue().parse("Sandro;10");
        assertEquals(dataSample.getValue().getName(),"Sandro");
        assertEquals(dataSample.getValue().getAge(),10);
    }
}

class DataSample {

    @CsvColumn(0)
    private String name;

    @CsvColumn(1)
    private Integer age;

    public DataSample(
            final String name,
            final Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}

