package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleParserTest {

    /*
     * GIVEN a DoubleParser
     *  AND a valid double string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the double
     */
    @Test
    public void parsing_valid_double_return_success(){
        // GIVEN a CharacterParser
        final var parser = DoubleParser.getInstance();
        // AND a valid character string S
        final var string = "0.0";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        Assertions.assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, 0.0);
    }

    /*
     * GIVEN a DoubleParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a CharacterParser
        final var parser = DoubleParser.getInstance();
        // AND a valid local date string
        final var string = "";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        Assertions.assertTrue(result.isSuccess());
        // AND the value null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a DoubleParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a DoubleParser
        final var parser = DoubleParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        Assertions.assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a DoubleParser
     *  AND a invalid double string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type NumberFormatException
     *  AND the message is formatted with S, Double.class, and the cause
     */
    @Test
    public void parsing_invalid_double_return_success(){
        // GIVEN a DoubleParser
        final var parser = DoubleParser.getInstance();
        // AND a valid local date string
        final var string = "THIS IS NOT VALID";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Failure
        Assertions.assertTrue(result.isFailure());
        // AND the exception is of the type ParserException
        final var exception = result.getException();
        Assertions.assertEquals(exception.getClass(), ParserException.class);
        // AND the cause is of the type NumberFormatException
        final var cause = exception.getCause();
        Assertions.assertEquals(cause.getClass(), NumberFormatException.class);
        // AND the message is formatted with S, Double.class, and the cause
        Assertions.assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, Double.class.getName(), cause.toString()));
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(DoubleParser.getInstance(), DoubleParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<DoubleParser> constructor = DoubleParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
