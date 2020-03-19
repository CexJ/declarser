package kernel.stages.stage04_toobject.impl;

import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;
import kernel.stages.stage04_toobject.impl.exceptions.OutputGluingException;
import kernel.tryapi.Try;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToObjectTest {

    private static final class TypeK{}
    private final TypeK k = new TypeK();
    private static final class TypeT{}
    private final TypeT t = new TypeT();
    private static final class TypeO{}

    /*
     * GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
     *  AND a value O of TypeO
     *  AND a Restructor R that return O from M
     *  AND a Validator V that succeed when passed O
     *  AND a ToObject constructed with R and V
     * WHEN the gluing method is invoked with M
     * THEN the result is a Success
     *  AND the value is O
     */
    @Test
    public void gluing_valid_field_return_success(){
        // GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
        final var map = new HashMap<TypeK, Object>();
        map.put(k,t);
        // AND a value O of TypeO
        final var output = new TypeO();
        // AND a Restructor R that return O from M
        final Restructor<TypeK, TypeO> restructor = m -> Try.success(output);
        // AND a Validator V that succeed when passed O
        final Validator<TypeO> validator = o -> Optional.empty();
        // AND a ToObject constructed with R and V
        final var toObject = ToObjectImpl.of(validator,restructor);
        // WHEN the gluing method is invoked with M
        final var result = toObject.gluing(map);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is O
        final var value = result.getValue();
        assertEquals(value, output);
    }



    /*
     * GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
     *  AND a value O of TypeO
     *  AND an Exception E
     *  AND a Validator V that fail when passed O with E
     *  AND a Restructor R that return O from M
     *  AND a ToObject constructed with R and V
     * WHEN the gluing method is invoked with M
     * THEN the result is a Failure
     *  AND the Exception is of the type OutputGluingException
     *  AND the cause is E
     *  AND the message is formatted with M and E
     */
    @Test
    public void gluing_invalid_field_return_failure(){
        // GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
        final var map = new HashMap<TypeK, Object>();
        map.put(k,t);
        // AND a value O of TypeO
        final var output = new TypeO();
        // AND an Exception E
        final var exception = new Exception("Invalid output");
        // AND a Validator V that fail when passed O with E
        final Validator<TypeO> validator = o -> Optional.of(exception);
        // AND a Restructor R that return O from M
        final Restructor<TypeK, TypeO> restructor = m -> Try.success(output);
        // AND a ToObject constructed with R and V
        final var toObject = ToObjectImpl.of(validator,restructor);
        // WHEN the gluing method is invoked with M
        final var result = toObject.gluing(map);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the Exception is of the type OutputGluingException
        final var resultException = result.getException();
        assertEquals(resultException.getClass(), OutputGluingException.class);
        // AND the cause is E
        assertEquals(resultException.getCause(), exception);
        // AND the message is formatted with M and E
        assertEquals(resultException.getMessage(),
                String.format(OutputGluingException.messageFormatter, exception.getMessage(), map.toString()));
    }


    /*
     * GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
     *  AND an Exception E
     *  AND a Restructor R that return Failure(E) from M
     *  AND a ToObject constructed with R
     * WHEN the gluing method is invoked with M
     * THEN the result is a Failure
     *  AND the Exception is of the type OutputGluingException
     *  AND the cause is E
     *  AND the message is formatted with M and E
     */
    @Test
    public void gluing_inrestructeble_field_return_failure(){
        // GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
        final var map = new HashMap<TypeK, Object>();
        map.put(k,t);
        // AND an Exception E
        final var exception = new Exception("Invalid output");
        // AND a Restructor R that return Failure(E) from M
        final Restructor<TypeK, TypeO> restructor = m -> Try.fail(exception);
        // AND a ToObject constructed with R
        final var toObject = ToObjectImpl.of(o -> Optional.empty(),restructor);
        // WHEN the gluing method is invoked with M
        final var result = toObject.gluing(map);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the Exception is of the type OutputGluingException
        final var resultException = result.getException();
        assertEquals(resultException.getClass(), OutputGluingException.class);
        // AND the cause is E
        assertEquals(resultException.getCause(), exception);
        // AND the message is formatted with M and E
        assertEquals(resultException.getMessage(),
                String.format(OutputGluingException.messageFormatter, exception.getMessage(), map.toString()));
    }


}
