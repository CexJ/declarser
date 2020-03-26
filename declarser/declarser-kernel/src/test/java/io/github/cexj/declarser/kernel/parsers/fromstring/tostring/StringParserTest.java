package io.github.cexj.declarser.kernel.parsers.fromstring.tostring;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class StringParserTest {

    /*
     * GIVEN a StringParser
     *  AND a string S
     * WHEN the method apply is invoked with S
     * THEN the result is a Success
     *  AND the value is S
     */
    @Test
    public void parsing_valid_short_return_success(){
        // GIVEN a ShortParser
        final var parser = StringParser.getInstance();
        // AND a valid character string S
        final var string = "this is a string";
        // WHEN the method apply is invoked with S
        final var result = parser.apply(string);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is true
        final var value = result.getValue();
        assertEquals(value, string);
    }

    @Test
    public void testFlyWeightPattern(){
        assertEquals(StringParser.getInstance(), StringParser.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<StringParser> constructor = StringParser.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
