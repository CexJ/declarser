package io.github.cexj.declarser.kernel.validations.impl.fromstring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StringPatternValidatorTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(StringPatternValidator.getInstance("pattern"), StringPatternValidator.getInstance("pattern"));
        assertNotEquals(StringPatternValidator.getInstance("pattern1"), StringPatternValidator.getInstance("pattern2"));
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<StringPatternValidator> constructor = StringPatternValidator.class.getDeclaredConstructor(String.class);
        Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance("test");
    }

    /*
     * GIVEN a pattern P
     *  AND a valid input string I
     *  AND a StringPatternValidator V constructed with P
     * WHEN the method apply is invoked with I
     * THEN the result is empty
     */
    @Test
    public void validate_valid_string_return_empty(){
        // GIVEN a pattern P
        final var pattern = "[a-z]*";
        // AND a valid input string I
        final var input = "ok";
        // AND a StringPatternValidator V
        final var validator = StringPatternValidator.getInstance(pattern);
        // WHEN the method apply is invoked with I
        final var result = validator.apply(input);
        // THEN the result is empty
        assertTrue(result.isEmpty());
    }


    /*
     * GIVEN a pattern P
     *  AND an invalid input string I
     *  AND a StringPatternValidator V constructed with P
     * WHEN the method apply is invoked with I
     * THEN the result is present
     *  AND the exception is of type NonMathingPatternStringException
     *  AND the message is formatted with I
     */
    @Test
    public void validate_empty_string_return_non_empty(){
        // GIVEN a pattern P
        final var pattern = "a-z";
        // AND an invalid input string I
        final var input = "123";
        // AND a StringPatternValidator V
        final var validator = StringPatternValidator.getInstance(pattern);
        // WHEN the method apply is invoked with I
        final var result = validator.apply(input);
        // THEN the result is present
        assertTrue(result.isPresent());
        // AND the exception is of type NonMathingPatternStringException
        final var exception = result.get();
        assertEquals(exception.getClass(), StringPatternValidator.NonMathingPatternStringException.class);
        // AND the message is formatted with I
        assertEquals(exception.getMessage(), String.format(StringPatternValidator.NonMathingPatternStringException.messageFormatter,pattern, input));
    }

    /*
     * GIVEN a StringPatternValidator V
     * WHEN the method apply is invoked with null
     * THEN the result is present
     *  AND the exception is of type NonMathingPatternStringException
     *  AND the message is formatted with null
     */
    @Test
    public void validate_null_string_return_non_empty(){
        // GIVEN a StringPatternValidator V
        final var pattern = "[a-z]*";
        final var validator = StringPatternValidator.getInstance(pattern);
        // WHEN the method apply is invoked with null
        final var result = validator.apply(null);
        // THEN the result is present
        assertTrue(result.isPresent());
        // AND the exception is of type NonMathingPatternStringException
        final var exception = result.get();
        assertEquals(exception.getClass(), StringPatternValidator.NonMathingPatternStringException.class);
        // AND the message is formatted with null
        assertEquals(exception.getMessage(), String.format(StringPatternValidator.NonMathingPatternStringException.messageFormatter,pattern, null));
    }
}
