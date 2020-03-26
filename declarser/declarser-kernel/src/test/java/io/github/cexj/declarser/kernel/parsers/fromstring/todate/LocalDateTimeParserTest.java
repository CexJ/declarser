package io.github.cexj.declarser.kernel.parsers.fromstring.todate;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateTimeParserTest {

    /*
     * GIVEN a LocalDateTime format F
     *  AND a LocalDateTimeParser constructed with F
     *  AND a valid local date string
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the date
     */
    @Test
    public void parsing_valid_local_date_time_return_success(){
        // GIVEN a LocalDateTime format F
        final var format = "yyyy-MM-dd HH:mm";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateTimeParser.getInstance(format);
        // AND a valid local dateTime string
        final var string = "2020-03-05 23:59";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertEquals(value, LocalDateTime.of(2020,3,5,23,59));
    }

    /*
     * GIVEN a LocalDateTime format F
     *  AND a LocalDateTimeParser constructed with F
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a LocalDateTime format F
        final var format = "yyyy-MM-dd HH:mm";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateTimeParser.getInstance(format);
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
     * GIVEN a LocalDateTime format F
     *  AND a LocalDateTimeParser constructed with F
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a LocalDateTime format F
        final var format = "yyyy-MM-dd HH:mm";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateTimeParser.getInstance(format);
        // WHEN the method apply is invoked with null
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a LocalDateTime format F
     *  AND a LocalDateTimeParser constructed with F
     *  AND a invalid local date time string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type DateTimeParseException
     *  AND the message is formatted with S, LocalDateTime.class, and the cause
     */
    @Test
    public void parsing_invalid_local_date_time_return_failure(){
        // GIVEN a LocalDateTime format F
        final var format = "yyyy-MM-dd HH:mm";
        // AND a LocalDateParser constructed with F
        final var parser = LocalDateTimeParser.getInstance(format);
        // AND a invalid local date time string
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
        // AND the message is formatted with S, LocalDateTime.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, LocalDateTime.class.getName(), cause.toString()));
    }


    @Test
    public void testFlyWeightPattern(){
        assertEquals(LocalDateTimeParser.getInstance("format"), LocalDateTimeParser.getInstance("format"));
        assertNotEquals(LocalDateTimeParser.getInstance("format"), LocalDateTimeParser.getInstance("not the same format"));
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<LocalDateTimeParser> constructor = LocalDateTimeParser.class.getDeclaredConstructor(String.class);
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance("format");
    }
}
