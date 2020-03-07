package kernel.parsers.fromstring.toprimitive;

import kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class ShortParserTest {

    /*
     * GIVEN a ShortParser
     *  AND a valid double string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the float
     */
    @Test
    public void parsing_valid_short_return_success(){
        // GIVEN a ShortParser
        final var parser = ShortParser.getInstance();
        // AND a valid character string S
        final var string = "0";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        short shortValue = 0;
        assertEquals(value, shortValue);
    }

    /*
     * GIVEN a ShortParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a ShortParser
        final var parser = ShortParser.getInstance();
        // AND a valid local date string
        final var string = "";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a ShortParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a ShortParser
        final var parser = ShortParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a ShortParser
     *  AND a invalid Integer string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type NumberFormatException
     *  AND the message is formatted with S, Short.class, and the cause
     */
    @Test
    public void parsing_invalid_short_return_success(){
        // GIVEN a ShortParser
        final var parser = ShortParser.getInstance();
        // AND a valid local date string
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
        // AND the message is formatted with S, Short.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, Short.class.toString(), cause.toString()));
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(ShortParser.getInstance(), ShortParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ShortParser> constructor = ShortParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
