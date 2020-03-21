package examples.subset.defaultfactory;

import csv.CsvDeclarserFactory;
import examples.subset.samples.SubsetExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubsetDefaultExampleTest {

    @Test
    public void parse_contained_input_return_success(){
        final var csv = "first";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
    }

    @Test
    public void parse_bijective_input_return_success(){
        final var csv = "first;second";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
        assertEquals(value.getSecondString(),"second");
    }

    @Test
    public void parse_contain_input_return_failure(){
        final var csv = "first;second;third";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
        assertEquals(value.getSecondString(),"second");
    }



}
