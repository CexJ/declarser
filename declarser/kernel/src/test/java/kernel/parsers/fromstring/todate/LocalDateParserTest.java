package kernel.parsers.fromstring.todate;

import kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateParserTest {

    /*
     * GIVEN a LocalDate format F
     *  AND a LocalDateParser constructed with F
     *  AND a valid local date string
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the date
     */
    @Test
    public void parsing_valid_local_date_return_success(){
        // GIVEN a LocalDate format F
        final var format = "yyyy-MM-dd";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateParser.getInstance(format);
        // AND a valid local date string
        final var string = "2020-03-05";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertEquals(value, LocalDate.of(2020,3,5));
    }

    /*
     * GIVEN a LocalDate format F
     *  AND a LocalDateParser constructed with F
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a LocalDate format F
        final var format = "yyyy-MM-dd";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateParser.getInstance(format);
        // AND a valid local date string
        final var string = "";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a LocalDate format F
     *  AND a LocalDateParser constructed with F
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a LocalDate format F
        final var format = "yyyy-MM-dd";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateParser.getInstance(format);
        // WHEN the method apply is invoked with null
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a LocalDate format F
     *  AND a LocalDateParser constructed with F
     *  AND a invalid local date string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type DateTimeParseException
     *  AND the message is formatted with S, LocalDate.class, and the cause
     */
    @Test
    public void parsing_invalid_local_date_return_failure(){
        // GIVEN a LocalDate format F
        final var format = "yyyy-MM-dd";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateParser.getInstance(format);
        // AND a valid local date string
        final var string = "THIS IS NOT VALID";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the exception is of the type ParserException
        final var exception = result.getException();
        assertEquals(exception.getClass(), ParserException.class);
        // AND the cause is of the type DateTimeParseException
        final var cause = exception.getCause();
        assertEquals(cause.getClass(), DateTimeParseException.class);
        // AND the message is formatted with S, LocalDate.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, LocalDate.class.toString(), cause.toString()));


    }
}
