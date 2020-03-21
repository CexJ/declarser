package examples.prevalidation.dynamic;

import csv.CsvDeclarserFactory;
import examples.prevalidation.dynamic.samples.DynamicPrevalidationExample;
import kernel.validations.Validator;
import kernel.validations.impl.fromstring.StringPatternValidator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicPrevalidationExampleTest {

    @Test
    public void parse_valid_input_return_success(){
        final var csv = "First;second";
        final var preValidationMap = new HashMap<Class<? extends Validator<String>>,
                Function<String[], Validator<String>>>();
        preValidationMap.put(StringPatternValidator.class, strings -> StringPatternValidator.getInstance("[A-Z][a-z]*"));

        final var declarserFactory = CsvDeclarserFactory.builder()
                .withCustomPreValidatorsMap(preValidationMap)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(DynamicPrevalidationExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"First");
        assertEquals(value.getSecondString(),"second");
    }

    @Test
    public void parse_invalid_input_return_failure(){
        final var csv = "first;second";
        final var preValidationMap = new HashMap<Class<? extends Validator<String>>,
                Function<String[], Validator<String>>>();
        preValidationMap.put(StringPatternValidator.class, strings -> StringPatternValidator.getInstance("[A-Z][a-z]*"));

        final var declarserFactory = CsvDeclarserFactory.builder()
                .withCustomPreValidatorsMap(preValidationMap)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(DynamicPrevalidationExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isFailure());
    }
}
