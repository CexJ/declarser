package kernel.parsers.fromstring.tonumber;

import kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BigIntegerParserTest {

    /*
     * GIVEN a BigIntegerParser
     *  AND a valid BigInteger string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the date
     */
    @Test
    public void parsing_valid_big_integer_return_success(){
        // GIVEN a BigIntegerParser constructed
        final var parser = BigIntegerParser.getInstance();
        // AND a valid local date string
        final var string = "0";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertEquals(value, new BigInteger("0"));
    }

    /*
     * GIVEN a BigIntegerParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a BigIntegerParser constructed
        final var parser = BigIntegerParser.getInstance();
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
     * GIVEN a BigIntegerParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a BigIntegerParser constructed
        final var parser = BigIntegerParser.getInstance();
        // WHEN the method apply is invoked with null
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is the date
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a BigIntegerParser
     *  AND a invalid BigInteger string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type NumberFormatException
     *  AND the message is formatted with S, BigInteger.class, and the cause
     */
    @Test
    public void parsing_invalid_big_integer_return_failure(){
        // GIVEN a BigIntegerParser constructed
        final var parser = BigIntegerParser.getInstance();
        // AND a invalid BigInteger string
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
        // AND the message is formatted with S, BigInteger.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, BigInteger.class.toString(), cause.toString()));
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(BigIntegerParser.getInstance(), BigIntegerParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<BigIntegerParser> constructor = BigIntegerParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
