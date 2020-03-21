package examples.array;

import csv.CsvDeclarserFactory;
import examples.array.samples.ArrayExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayExampleTest {

    @Test
    public void parse_valid_csv_return_success() {
        final var csv = "John Bob";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(ArrayExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getaStringArray().length,2);
        assertEquals(value.getaStringArray()[0],"John");
        assertEquals(value.getaStringArray()[1],"Bob");

    }
}
