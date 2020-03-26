package examples.custom;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import examples.custom.parsers.DynamicIntegerGreaterThanParser;
import examples.custom.samples.CustomDynamicExample;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomDynamicExampleTest {

    @Test
    public void parse_valid_input_custom_transformer_return_success() {
        final var csv = "11";
        final var customConstructorMap = new HashMap<Class<? extends Parser<String,?>>,
                Function<String[], Parser<String,?>>>();

        customConstructorMap.put(DynamicIntegerGreaterThanParser.class,
                s -> new DynamicIntegerGreaterThanParser(10));

        final var declarserFactory = CsvDeclarserFactory.builder()
                .withCustomConstructorMap(customConstructorMap).build();
        final var tryDeclarser = declarserFactory.declarserOf(CustomDynamicExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getAnInteger(),11);
    }

    @Test
    public void parse_invalid_input_custom_transformer_return_failure() {
        final var csv = "9";
        final var customConstructorMap = new HashMap<Class<? extends Parser<String,?>>,
                Function<String[], Parser<String,?>>>();

        customConstructorMap.put(DynamicIntegerGreaterThanParser.class,
                s -> new DynamicIntegerGreaterThanParser(10));

        final var declarserFactory = CsvDeclarserFactory.builder()
                .withCustomConstructorMap(customConstructorMap).build();
        final var tryDeclarser = declarserFactory.declarserOf(CustomDynamicExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isFailure());
    }

}
