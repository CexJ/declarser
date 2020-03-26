package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class FloatParserTest {

    /*
     * GIVEN a FloatParser
     *  AND a valid double string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the float
     */
    @Test
    public void parsing_valid_float_return_success(){
        // GIVEN a FloatParser
        final var parser = FloatParser.getInstance();
        // AND a valid character string S
        final var string = "0.0f";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, 0.0f);
    }

    /*
     * GIVEN a FloatParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a CharacterParser
        final var parser = FloatParser.getInstance();
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
     * GIVEN a FloatParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a FloatParser
        final var parser = FloatParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a FloatParser
     *  AND a invalid float string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type NumberFormatException
     *  AND the message is formatted with S, Float.class, and the cause
     */
    @Test
    public void parsing_invalid_float_return_success(){
        // GIVEN a FloatParser
        final var parser = FloatParser.getInstance();
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
        // AND the message is formatted with S, Float.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, Float.class.getName(), cause.toString()));
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(FloatParser.getInstance(), FloatParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<FloatParser> constructor = FloatParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
