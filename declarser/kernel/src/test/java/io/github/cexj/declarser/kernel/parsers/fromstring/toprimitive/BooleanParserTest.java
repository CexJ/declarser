package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanParserTest {


    /*
     * GIVEN a BooleanParser
     *  AND a valid true boolean string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is the boolean
     */
    @ParameterizedTest
    @ValueSource(strings = { "TRUE", "true", "True", "TruE"})
    public void parsing_valid_true_return_success(String string){
        // GIVEN a BooleanParser
        final var parser = BooleanParser.getInstance();
        // AND a valid true boolean string S
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, Boolean.TRUE);
    }

    /*
     * GIVEN a BooleanParser
     *  AND a valid false boolean string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is false
     */
    @ParameterizedTest
    @ValueSource(strings = { "FALSE", "false", "False", "FalsE"})
    public void parsing_valid_false_return_success(String string){
        // GIVEN a BooleanParser
        final var parser = BooleanParser.getInstance();
        // AND a valid false boolean string S
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is false
        final var value = result.getValue();
        assertEquals(value, Boolean.FALSE);
    }

    /*
     * GIVEN a BooleanParser
     *  AND an empty string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_empty_string_return_success(){
        // GIVEN a BooleanParser
        final var parser = BooleanParser.getInstance();
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
     * GIVEN a BooleanParser
     * WHEN the method apply is invoked with null
     * THEN the result is a Success
     *  AND the value is null
     */
    @Test
    public void parsing_null_return_success(){
        // GIVEN a BooleanParser
        final var parser = BooleanParser.getInstance();
        // WHEN the method apply is invoked with S
        final var result = parser.apply(null);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is null
        final var value = result.getValue();
        assertNull(value);
    }

    /*
     * GIVEN a BooleanParser
     *  AND a invalid boolean string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is false
     */
    @Test
    public void parsing_invalid_boolean_return_success(){
        // GIVEN a BooleanParser
        final var parser = BooleanParser.getInstance();
        // AND a valid local date string
        final var string = "THIS IS NOT VALID";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is false
        final var value = result.getValue();
        assertEquals(value, Boolean.FALSE);
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(BooleanParser.getInstance(), BooleanParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<BooleanParser> constructor = BooleanParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
