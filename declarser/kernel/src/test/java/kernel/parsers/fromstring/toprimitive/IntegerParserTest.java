package kernel.parsers.fromstring.toprimitive;

import kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerParserTest {

    /*
     * GIVEN a IntegerParser
     *  AND a valid double string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the float
     */
    @Test
    public void parsing_valid_integer_return_success(){
        // GIVEN a IntegerParser
        final var parser = IntegerParser.getInstance();
        // AND a valid character string S
        final var string = "0";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, 0);
    }

    /*
     * GIVEN a IntegerParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a IntegerParser
        final var parser = IntegerParser.getInstance();
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
     * GIVEN a IntegerParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a IntegerParser
        final var parser = IntegerParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a IntegerParser
     *  AND a invalid Integer string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type NumberFormatException
     *  AND the message is formatted with S, Integer.class, and the cause
     */
    @Test
    public void parsing_invalid_integer_return_success(){
        // GIVEN a IntegerParser
        final var parser = IntegerParser.getInstance();
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
        // AND the message is formatted with S, Integer.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, Integer.class.toString(), cause.toString()));
    }
}
