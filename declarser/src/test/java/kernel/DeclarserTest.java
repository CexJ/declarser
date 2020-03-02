package kernel;

import kernel.stages.stage01_tomap.ToMapImpl;
import kernel.stages.stage02_totypedmap.ToTypedMapImpl;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.ToObjectImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.tryapi.Try;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
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
        ToMapImpl<TypeI, TypeK, TypeV> toMap = Mockito.mock(ToMapImpl.class);
        when(toMap.mapping(input)).thenReturn(Try.success(map));
        // AND a ToTypedMap DTM that type M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        ToTypedMapImpl<TypeK, TypeV> toTypedMap = Mockito.mock(ToTypedMapImpl.class);
        when(toTypedMap.typing(map)).thenReturn(typedMap);
        // AND a Combinator DC that compose TM into a map CM
        final var combinedMap = new HashMap<TypeK,Object>();
        Combinator<TypeK> combinator = Mockito.mock(Combinator.class);
        when(combinator.combining(typedMap)).thenReturn(Try.success(combinedMap));
        // AND a ToObject that DO glue CM into a value O of TypeO
        final var output = new TypeO();
        ToObjectImpl<TypeK,TypeO> toObject = Mockito.mock(ToObjectImpl.class);
        when(toObject.gluing(combinedMap)).thenReturn(Try.success(output));
        // AND a Declarser created from DM, DTM, DC, and DO
        final var declarser = Declarser.of(toMap,toTypedMap,combinator,toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.parse(input);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is O
        final var value = result.getValue();
        assertEquals(value, output);
    }
}
