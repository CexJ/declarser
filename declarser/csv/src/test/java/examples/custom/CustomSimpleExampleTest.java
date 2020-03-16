package examples.custom;

import csv.CsvDeclarserFactory;
import examples.auto.samples.AutoSimpleExample;
import examples.custom.samples.CustomSimpleExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomSimpleExampleTest {

    @Test
    public void parse_valid_input_custom_transformer_return_success() {
        final var csv = "1";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(CustomSimpleExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.parse(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getAnInteger(),1);
    }

    @Test
    public void parse_invalid_input_custom_transformer_return_failure() {
        final var csv = "-1";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(CustomSimpleExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.parse(csv);
        assertTrue(result.isFailure());
    }
}
