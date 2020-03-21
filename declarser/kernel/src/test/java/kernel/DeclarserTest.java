package kernel;

import kernel.stages.stage01_tomap.impl.ToMap;
import kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.ToObject;
import org.junit.jupiter.api.Test;
import kernel.exceptions.DeclarserException;
import kernel.tryapi.Try;

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
     *  AND a ToMap DM that maps I into a map M
     *  AND a ToTypedMap DTM that types M into a map TM
     *  AND a Combinator DC that composes TM into a map CM
     *  AND a ToObject DO that glues CM into a value O of TypeO
     *  AND a Declarser created from DM, DTM, DC, and DO
     * WHEN the parse method is invoked with I
     * THEN the result is a Success
     *  AND the value is O
     */
    @Test
    public void parsing_valid_input_return_a_success(){
        // GIVEN a value I of type TypeI
        final var input = new TypeI();
        // AND a ToMap DM that maps I into a map M
        final var map = new HashMap<TypeK,TypeV>();
        final ToMap<TypeI, TypeK, TypeV> toMap = i ->
                input.equals(i) ? Try.success(map)
                                : Try.fail(new Exception("ToMap exception"));
        // AND a ToTypedMap DTM that types M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        final ToTypedMap<TypeK, TypeV> toTypedMap = m -> typedMap;
        // AND a Combinator DC that composes TM into a map CM
        final var combinedMap = new HashMap<TypeK,Object>();
        final Combinator<TypeK> combinator = tm ->
                typedMap.equals(tm) ? Try.success(combinedMap)
                                    : Try.fail(new Exception("Combinator exception"));
        // AND a ToObject DO that glues CM into a value O of TypeO
        final var output = new TypeO();
        final ToObject<TypeK, TypeO> toObject = cm ->
                combinedMap.equals(cm) ? Try.success(output)
                                       : Try.fail(new Exception("ToObject exception"));
        // AND a Declarser created from DM, DTM, DC, and DO
        final var declarser = DeclarserImpl.of(toMap, toTypedMap, combinator, toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.apply(input);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        // AND the value is O
        final var value = result.getValue();
        assertEquals(value, output);
    }


    /*
     * GIVEN a value I of type TypeI
     *  AND an Exception E
     *  AND a ToMap DM that maps I into a map M
     *  AND a ToTypedMap DTM that types M into a map TM
     *  AND a Combinator DC that composes TM into a map CM
     *  AND a ToObject DO that fails with E when try to glue CM
     *  AND a Declarser created from DM, DTM, DC, and DO
     * WHEN the parse method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception type is DeclarserException
     *  AND the cause Exception
     *  AND the message is formatted with I and E
     */
    @Test
    public void parsing_inglueable_input_return_a_failure(){
        // GIVEN a value I of type TypeI
        final var input = new TypeI();
        // AND an Exception E
        final var exception = new Exception("ToTypedMap exception");
        // AND a ToMap DM that map I into a map M
        final var map = new HashMap<TypeK,TypeV>();
        final ToMap<TypeI, TypeK, TypeV> toMap = i ->
                input.equals(i) ? Try.success(map)
                                : Try.fail(new Exception("ToMap exception"));
        // AND a ToTypedMap DTM that type M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        final ToTypedMap<TypeK, TypeV> toTypedMap = m -> typedMap;
        // AND a Combinator DC that compose TM into a map CM
        final var combinedMap = new HashMap<TypeK,Object>();
        final Combinator<TypeK> combinator = tm ->
                typedMap.equals(tm) ? Try.success(combinedMap)
                                    : Try.fail(new Exception("Combinator exception"));
        // AND a ToObject DO that fails with E when try to glue CM
        final var output = new TypeO();
        final ToObject<TypeK, TypeO> toObject = cm ->
                combinedMap.equals(cm) ? Try.fail(exception)
                        : Try.success(output);
        // AND a Declarser created from DM, DTM, DC, and DO
        final var declarser = DeclarserImpl.of(toMap, toTypedMap, combinator, toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.apply(input);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the Exception type is DeclarserException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), DeclarserException.class);
        // AND the cause Exception
        final var cause = exceptionResult.getCause();
        assertEquals(cause, exception);
        // AND the message is formatted with I and E
        final var message = exceptionResult.getMessage();
        assertEquals(message,
                String.format(DeclarserException.messageFormatter, exception.getMessage(), input.toString()));
    }


    /*
     * GIVEN a value I of type TypeI
     *  AND an Exception E
     *  AND a ToMap DM that maps I into a map M
     *  AND a ToTypedMap DTM that types M into a map TM
     *  AND a Combinator DC that fails with E when try to compose TM
     *  AND a Declarser created from DM, DTM, and DC
     * WHEN the parse method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception type is DeclarserException
     *  AND the cause Exception
     *  AND the message is formatted with I and E
     */
    @Test
    public void parsing_incomposable_input_return_a_failure(){
        // GIVEN a value I of type TypeI
        final var input = new TypeI();
        // AND an Exception E
        final var exception = new Exception("Combinator exception");
        // AND a ToMap DM that map I into a map M
        final var map = new HashMap<TypeK,TypeV>();
        final ToMap<TypeI, TypeK, TypeV> toMap = i ->
                input.equals(i) ? Try.success(map)
                        : Try.fail(new Exception("ToMap exception"));
        // AND a ToTypedMap DTM that type M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        final ToTypedMap<TypeK, TypeV> toTypedMap = m -> typedMap;
        // AND a Combinator DC that fails with E when try to compose DTM
        final var combinedMap = new HashMap<TypeK,Object>();
        final Combinator<TypeK> combinator = tm ->
                typedMap.equals(tm) ? Try.fail(exception)
                                    : Try.success(combinedMap);
        // AND a ToObject DO that fails with E when try to glue CM
        final var output = new TypeO();
        final ToObject<TypeK, TypeO> toObject = cm ->
                combinedMap.equals(cm) ? Try.success(output)
                                       : Try.fail(new Exception("ToObject exception"));
        // AND a Declarser created from DM, DTM, and DC
        final var declarser = DeclarserImpl.of(toMap, toTypedMap, combinator, toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.apply(input);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the Exception type is DeclarserException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), DeclarserException.class);
        // AND the cause Exception
        final var cause = exceptionResult.getCause();
        assertEquals(cause, exception);
        // AND the message is formatted with I and E
        final var message = exceptionResult.getMessage();
        assertEquals(message,
                String.format(DeclarserException.messageFormatter, exception.getMessage(), input.toString()));
    }

    /*
     * GIVEN a value I of type TypeI
     *  AND an Exception E
     *  AND a ToMap DM that fails with E when try to map I
     *  AND a Declarser created from DM
     * WHEN the parse method is invoked with I
     * THEN the result is a Failure
     *  AND the Exception type is DeclarserException
     *  AND the cause Exception
     *  AND the message is formatted with I and E
     */
    @Test
    public void parsing_inmappable_input_return_a_failure(){
        // GIVEN a value I of type TypeI
        final var input = new TypeI();
        // AND an Exception E
        final var exception = new Exception("ToMap exception");
        // AND a ToMap DM that map I into a map M
        final var map = new HashMap<TypeK,TypeV>();
        final ToMap<TypeI, TypeK, TypeV> toMap = i ->
                input.equals(i) ? Try.fail(exception)
                                : Try.success(map);
        // AND a ToTypedMap DTM that type M into a map TM
        final var typedMap = new HashMap<TypeK,Try<?>>();
        final ToTypedMap<TypeK, TypeV> toTypedMap = m -> typedMap;
        // AND a Combinator DC that fails with E when try to compose DTM
        final var combinedMap = new HashMap<TypeK,Object>();
        final Combinator<TypeK> combinator = tm ->
                typedMap.equals(tm) ? Try.success(combinedMap)
                                    : Try.fail(new Exception("Combinator exception"));
        // AND a ToObject DO that fails with E when try to glue CM
        final var output = new TypeO();
        final ToObject<TypeK, TypeO> toObject = cm ->
                combinedMap.equals(cm) ? Try.success(output)
                                       : Try.fail(new Exception("ToObject exception"));
        // AND a Declarser created from DM, DTM, and DC
        final var declarser = DeclarserImpl.of(toMap, toTypedMap, combinator, toObject);
        // WHEN the parse method is invoked with I
        final var result = declarser.apply(input);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        // AND the Exception type is DeclarserException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), DeclarserException.class);
        // AND the cause Exception
        final var cause = exceptionResult.getCause();
        assertEquals(cause, exception);
        // AND the message is formatted with I and E
        final var message = exceptionResult.getMessage();
        assertEquals(message,
                String.format(DeclarserException.messageFormatter, exception.getMessage(), input.toString()));
    }

}
