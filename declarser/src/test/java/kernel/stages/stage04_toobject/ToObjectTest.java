package kernel.stages.stage04_toobject;

import org.junit.jupiter.api.Test;

public class ToObjectTest {

    /*
     * GIVEN a map M from TypeK to ? that contains an Entry (K, T: TypeT)
     *  AND a value O of TypeO
     *  AND an Exception E
     *  AND a Validator V that fail when passed O with E
     *  AND a Destructor D that return O from M
     *  AND a ToObject constructed with V
     * WHEN the gluing method is invoked with M
     * THEN the result is a Failure
     *  AND the Exception is of the type OutputValidationException
     *  AND the cause is E
     *  AND the message is formatted with O and E
     */
    @Test
    public void gluing_invalid_field_return_failure(){

    }
}
