package kernel;

import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.ToObject;
import org.junit.jupiter.api.Test;
import utils.tryapi.Try;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeclarserTest {

    private static final class TypeI{}
    private static final class TypeO{}
    private static final class TypeK{}
    private static final class TypeV{}

    /*
     * GIVEN a value I of type TypeI
     *  AND a ToMap DM that map I into a map M
     *  AND a ToTypedMap DTM that type M into a map TM
     *  AND a Combinator DC that compose TM into a map CM
     *  AND a ToObject that DO glue CM into a value O of TypeO
     *  AND a Declarser created from DM, DTM, DC, and DO
     * WHEN the parse method is invoked with I
     * THEN the result is a Success
     *  AND the value is O
     */
    @Test
    public void parsing_valid_input_return_a_success(){
        // GIVEN a value I of type TypeI
        final var input = new TypeI();
        // AND a ToMap DM that map I into a map M
        final var map = new HashMap<TypeK,TypeV>();
        final ToMap<TypeI, TypeK, TypeV> toMap = i -> input.equals(i) ? Try.success(map) : Try.fail(new Exception("ToMap exception"));
        // AND a ToTypedMap DTM that type M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        final ToTypedMap<TypeK, TypeV> toTypedMap = m -> typedMap;
        // AND a Combinator DC that compose TM into a map CM
        final var combinedMap = new HashMap<TypeK,Object>();
        final Combinator<TypeK> combinator = tm -> typedMap.equals(tm) ? Try.success(combinedMap) : Try.fail(new Exception("Combinator exception"));
        // AND a ToObject that DO glue CM into a value O of TypeO
        final var output = new TypeO();
        final ToObject<TypeK, TypeO> toObject = cm ->  combinedMap.equals(cm) ? Try.success(output) : Try.fail(new Exception("ToObject exception"));
        // AND a Declarser created from DM, DTM, DC, and DO
        final var declarser = Declarser.of(toMap, toTypedMap, combinator, toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.parse(input);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is O
        final var value = result.getValue();
        assertEquals(value, output);
    }
}
