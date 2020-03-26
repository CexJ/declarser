package io.github.cexj.declarser.kernel.validations.impl.fromstring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonBlankStringValidatorTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(NonBlankStringValidator.getInstance(), NonBlankStringValidator.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<NonBlankStringValidator> constructor = NonBlankStringValidator.class.getDeclaredConstructor();
        Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /*
     * GIVEN a valid input string I
     *  AND a NonBlankStringValidator V
     * WHEN the method apply is invoked with I
     * THEN the result is empty
     */
    @Test
    public void validate_valid_string_return_empty(){
        // GIVEN a valid input string I
        final var input = "non blank";
        // AND a NonBlankStringValidator V
        final var validator = NonBlankStringValidator.getInstance();
        // WHEN the method apply is invoked with I
        final var result = validator.apply(input);
        // THEN the result is empty
        assertTrue(result.isEmpty());
    }


    /*
     * GIVEN a blank input string I
     *  AND a NonBlankStringValidator V
     * WHEN the method apply is invoked with I
     * THEN the result is present
     *  AND the exception is of type BlankStringException
     *  AND the message is formatted with I
     */
    @Test
    public void validate_blank_string_return_non_empty(){
        // GIVEN a blank input string I
        final var input = " ";
        // AND a NonBlankStringValidator V
        final var validator = NonBlankStringValidator.getInstance();
        // WHEN the method apply is invoked with I
        final var result = validator.apply(input);
        // THEN the result is present
        assertTrue(result.isPresent());
        // AND the exception is of type NonBlankStringValidator
        final var exception = result.get();
        assertEquals(exception.getClass(), NonBlankStringValidator.BlankStringException.class);
        // AND the message is formatted with I
        assertEquals(exception.getMessage(), String.format(NonBlankStringValidator.BlankStringException.messageFormatter, input));
    }

    /*
     * GIVEN a NonBlankStringValidator V
     * WHEN the method apply is invoked with null
     * THEN the result is present
     *  AND the exception is of type BlankStringException
     *  AND the message is formatted with null
     */
    @Test
    public void validate_null_string_return_non_empty(){
        // GIVEN a NonBlankStringValidator V
        final var validator = NonBlankStringValidator.getInstance();
        // WHEN the method apply is invoked with null
        final var result = validator.apply(null);
        // THEN the result is present
        assertTrue(result.isPresent());
        // AND the exception is of type NonBlankStringValidator
        final var exception = result.get();
        assertEquals(exception.getClass(), NonBlankStringValidator.BlankStringException.class);
        // AND the message is formatted with null
        assertEquals(exception.getMessage(), String.format(NonBlankStringValidator.BlankStringException.messageFormatter, (Object) null));
    }
}
