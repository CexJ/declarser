package kernel.stages.stage01_tomap;

import kernel.validation.Validator;

import org.junit.jupiter.api.Test;
import utils.exceptions.InputValidationException;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToMapTest {

    /*
     * GIVEN an input I
     *  AND a map M
     *  AND a ToMap that succeed when passed I with M
     * WHEN the mapping method is invoked with I
     * THEN the result is a Success
     *  AND the Map is M
     */
    @Test
    public void mapping_valid_input_returns_a_success(){
        // GIVEN an input I
        final Object input = new Object();
        // AND a map M
        final var map = new HashMap<>();
        // AND a ToMap that succeed when passed I with M
        final var toMap = ToMap.of(
                o -> Optional.empty(),
                i -> map);
        // WHEN the mapping method is invoked with I
        final var result = toMap.mapping(input);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the Map is M
        final var mapOutput = result.getValue();
        assertEquals(mapOutput,
                     map);
    }



    /*
     * GIVEN an input I
     *  AND an Exception E
     *  AND a Validator V that fail when passed I with E
     *  AND a ToMap constructed with V
     * WHEN the mapping method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception is of the type InputValidationException
     *  AND the cause is E
     *  AND the message is formatted with I and E
     */
    @Test
    public void mapping_invalid_input_returns_a_failure(){
        // GIVEN an input I
        final Object input = new Object();
        // AND an exception E
        final var exceptionInput = new Exception("Invalid input");
        // AND a validator V that fail when passed I with E
        final Validator<Object> validator = o -> Optional.of(exceptionInput);
        // AND a ToMap constructed with V
        final var toMap = ToMap.of(
                validator,
                i -> new HashMap<>());
        // WHEN the mapping method is invoked with I
        final var result = toMap.mapping(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type InputValidationException
        final var exceptionOutput = result.getException();
        assertEquals(exceptionOutput.getClass(),
                     InputValidationException.class);
        // AND the cause is E
        assertEquals(exceptionOutput.getCause(),
                     exceptionInput);
        // AND the message is formatted with I and E
        assertEquals(exceptionOutput.getMessage(),
                     String.format(InputValidationException.messageFormatter,
                        exceptionInput.toString(), input.toString()));
    }
}
