package kernel.parsers.fromstring.tonumber;

import kernel.parsers.exceptions.ParserException;
import kernel.parsers.fromstring.todate.ZonedDateTimeParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalParserTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(BigDecimalParser.getInstance(), BigDecimalParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<BigDecimalParser> constructor = BigDecimalParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /*
     * GIVEN a BigDecimalParser
     *  AND a valid BigDecimal string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the date
     */
    @Test
    public void parsing_valid_big_decimal_return_success(){
        // GIVEN a BigDecimalParser constructed
        final var parser = BigDecimalParser.getInstance();
        // AND a valid local date string
        final var string = "0.0";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertEquals(value, new BigDecimal("0.0"));
    }

    /*
     * GIVEN a BigDecimalParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a BigDecimalParser constructed
        final var parser = BigDecimalParser.getInstance();
        // AND an empty string
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
     * GIVEN a BigDecimalParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a BigDecimalParser constructed
        final var parser = BigDecimalParser.getInstance();
        // WHEN the method apply is invoked with null
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a BigDecimalParser
     *  AND a invalid BigDecimal string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type DateTimeParseException
     *  AND the message is formatted with S, BigDecimal.class, and the cause
     */
    @Test
    public void parsing_invalid_big_decimal_return_failure(){
        // GIVEN a BigDecimalParser constructed
        final var parser = BigDecimalParser.getInstance();
        // AND a invalid BigDecimal string
        final var string = "THIS IS NOT VALID";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the exception is of the type ParserException
        final var exception = result.getException();
        assertEquals(exception.getClass(), ParserException.class);
        // AND the cause is of the type NumberFormatException
        final var cause = exception.getCause();
        assertEquals(cause.getClass(), NumberFormatException.class);
        // AND the message is formatted with S, BigDecimal.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, BigDecimal.class.getName(), cause.toString()));
    }

}
