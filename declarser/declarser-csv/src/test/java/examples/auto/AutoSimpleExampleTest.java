package examples.auto;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import examples.auto.samples.AutoSimpleExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutoSimpleExampleTest {


    @Test
    public void parse_valid_csv_return_success() {
        final var csv = "John;50";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(AutoSimpleExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getName(),"John");
        assertEquals(value.getAge(),50);
    }

    @Test
    public void parse_empty_cell_return_null() {
        final var csv = ";";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(AutoSimpleExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertNull(value.getName());
        assertNull(value.getAge());
    }

    @Test
    public void parse_invalid_cell_return_failure() {
        final var csv = "John;notanumber";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(AutoSimpleExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isFailure());
    }
}

