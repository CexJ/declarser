package examples.postvalidation;

import csv.CsvDeclarserFactory;
import examples.postvalidation.samples.PostValidationExample;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PostValidationExampleTest {


    @Test
    public void parse_valid_csv_return_success() {
        final var csv = "10;20;50";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var exception = new Exception();
        final Validator<PostValidationExample> validator = example ->
                example.getSubQuantity1() + example.getSubQuantity2() > example.getTotalQuantity() ?
                        Optional.of(exception) : Optional.empty();
        final var tryDeclarser = declarserFactory.declarserOf(
                PostValidationExample.class,
                validator,
                ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getSubQuantity1(), 10);
        assertEquals(value.getSubQuantity2(), 20);
        assertEquals(value.getTotalQuantity(), 50);
    }


    @Test
    public void parse_invalid_csv_return_failure() {
        final var csv = "10;20;25";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var exception = new Exception();
        final Validator<PostValidationExample> validator = example ->
                example.getSubQuantity1() + example.getSubQuantity2() > example.getTotalQuantity() ?
                        Optional.of(exception) : Optional.empty();
        final var tryDeclarser = declarserFactory.declarserOf(
                PostValidationExample.class,
                validator,
                ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isFailure());
    }
}

