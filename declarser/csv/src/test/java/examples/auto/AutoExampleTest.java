package examples.auto;

import csv.CsvDeclarserFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoExampleTest {

    private static String name;
    private static int age;
    private static String separator;

    @BeforeAll
    public static void init(){
        name = "Jon";
        age = 50;
        separator = ";";
    }

    @Test
    public void parse_valid_csv_return_success() {
        final var csv = ""+name+separator+age;
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var declarser = declarserFactory.declarserOf(DataSample.class,  separator);
        final var dataSample = declarser.getValue().parse(csv);
        assertEquals(dataSample.getValue().getName(),name);
        assertEquals(dataSample.getValue().getAge(),age);
    }
}

