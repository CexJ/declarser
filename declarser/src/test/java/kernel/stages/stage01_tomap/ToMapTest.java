package kernel.stages.stage01_tomap;

import kernel.stages.stage01_tomap.destructor.Destructor;
import kernel.validation.Validator;

import org.junit.jupiter.api.Test;
import utils.exceptions.InputMappingException;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToMapTest {

    private static final class TypeI{}
    private static final class TypeK{}
    private static final class TypeV{}


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
        final var input = new TypeI();
        // AND a map M
        final var map = new HashMap<TypeK,TypeV>();
        // AND a ToMap that succeed when passed I with M
        final var toMap = ToMap.of(
                o -> Optional.empty(),
                i -> Try.success(map));
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
     *  AND a Destructor V that fail when passed I with E
     *  AND a ToMap constructed with V
     * WHEN the mapping method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception is of the type InputMappingException
     *  AND the cause is E
     *  AND the message is formatted with I and E
     */
    @Test
    public void mapping_indestructible_input_returns_a_success(){
        // GIVEN an input I
        final var input = new TypeI();
        // AND an exception E
        final var exceptionInput = new Exception("Invalid input");
        // AND a validator V that fail when passed I with E
        final Destructor<TypeI, TypeK, TypeV> destructor = o -> Try.fail(exceptionInput);
        // AND a ToMap constructed with V
        final var toMap = ToMap.of(
                o -> Optional.empty(),
                destructor);
        // WHEN the mapping method is invoked with I
        final var result = toMap.mapping(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type InputMappingException
        final var exceptionOutput = result.getException();
        assertEquals(exceptionOutput.getClass(),
                InputMappingException.class);
        // AND the cause is E
        assertEquals(exceptionOutput.getCause(),
                exceptionInput);
        // AND the message is formatted with I and E
        assertEquals(exceptionOutput.getMessage(),
                String.format(InputMappingException.messageFormatter,
                        exceptionInput.toString(), input.toString()));
    }


    /*
     * GIVEN an input I
     *  AND an Exception E
     *  AND a Validator V that fail when passed I with E
     *  AND a ToMap constructed with V
     * WHEN the mapping method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception is of the type InputMappingException
     *  AND the cause is E
     *  AND the message is formatted with I and E
     */
    @Test
    public void mapping_invalid_input_returns_a_failure(){
        // GIVEN an input I
        final var input = new TypeI();
        // AND an exception E
        final var exceptionInput = new Exception("Invalid input");
        // AND a validator V that fail when passed I with E
        final Validator<TypeI> validator = o -> Optional.of(exceptionInput);
        // AND a ToMap constructed with V
        final var toMap = ToMap.of(
                validator,
                i -> Try.success(new HashMap<>()));
        // WHEN the mapping method is invoked with I
        final var result = toMap.mapping(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type InputMappingException
        final var exceptionOutput = result.getException();
        assertEquals(exceptionOutput.getClass(),
                     InputMappingException.class);
        // AND the cause is E
        assertEquals(exceptionOutput.getCause(),
                     exceptionInput);
        // AND the message is formatted with I and E
        assertEquals(exceptionOutput.getMessage(),
                     String.format(InputMappingException.messageFormatter,
                        exceptionInput.toString(), input.toString()));
    }
}