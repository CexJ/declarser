package kernel.parsers.fromstring.toprimitive;

import kernel.parsers.exceptions.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterParserTest {

    /*
     * GIVEN a CharacterParser
     *  AND a valid character string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the character
     */
    @Test
    public void parsing_valid_character_return_success(){
        // GIVEN a CharacterParser
        final var parser = CharacterParser.getInstance();
        // AND a valid character string S
        final var string = "a";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, 'a');
    }

    /*
     * GIVEN a CharacterParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a CharacterParser
        final var parser = CharacterParser.getInstance();
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
     * GIVEN a CharacterParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a CharacterParser
        final var parser = CharacterParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a CharacterParser
     *  AND a invalid character string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Failure
     *  AND the exception is of the type ParserException
     *  AND the cause is of the type IllegalArgumentException
     *  AND the message is formatted with S, Character.class, and the cause
     */
    @Test
    public void parsing_invalid_character_return_success(){
        // GIVEN a CharacterParser
        final var parser = CharacterParser.getInstance();
        // AND a valid local date string
        final var string = "THIS IS NOT VALID";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the exception is of the type ParserException
        final var exception = result.getException();
        assertEquals(exception.getClass(), ParserException.class);
        // AND the cause is of the type IllegalArgumentException
        final var cause = exception.getCause();
        assertEquals(cause.getClass(), IllegalArgumentException.class);
        // AND the message is formatted with S, Character.class, and the cause
        assertEquals(exception.getMessage(), String.format(ParserException.messageFormatter,
                string, Character.class.toString(), cause.toString()));
    }
}
