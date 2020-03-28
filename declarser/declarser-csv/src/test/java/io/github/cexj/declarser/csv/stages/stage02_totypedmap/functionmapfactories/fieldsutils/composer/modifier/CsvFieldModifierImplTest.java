package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvArray;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldModifierImplTest {


    /*
     * GIVEN a value T of TypeT
     *  AND and exception E
     *  AND a string I
     *  AND a function F that map I into Success(I) and Fail(E) the rest
     *  AND a separator S
     *  AND a field FI with type array and the annotation CsvArray with separator S
     *  AND a csvFieldModifier CFM
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the operator transform the function F in NF such that
     *      NF maps I+S+I+....+S+I into Success([T,T,...,T])
     *      and Failure for the rest
     */
    @Test
    public void compute_a_valid_modifier_return_success() throws NoSuchFieldException {
        class TypeT{}
        // GIVEN a value T of TypeT
        final var value = new TypeT();
        // AND and exception E
        final var exception =  new Exception();
        // AND a string I
        final var input = "input";
        // AND a function F that map I into Success(I) and Fail(E) the rest
        final Parser<String,?> function = s -> input.equals(s) ? Try.success(value) : Try.fail(exception);
        // AND a separator S
        final var separator = "-";
        // AND a field FI with the annotation CsvArray with separator S
        class ModifierrSample {
            @SuppressWarnings("unused")
            @CsvArray(separator)
            private TypeT[] simpleData;
        }
        final var field = ModifierrSample.class.getDeclaredField("simpleData");
        // AND a csvFieldModifier CFM
        final var csvFieldModifier = CsvFieldModifier.getInstance();
        // WHEN the method compute is invoked with FI
        final var result = csvFieldModifier.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the operator transform the function F in NF such that
        //     NF maps I+S+I+....+S+I into Success([T,T,...,T])
        //     and Failure for the rest
        final var operator = result.getValue();
        final var modifiedFunction = operator.apply(function);
        final var arrayInput = input+separator+input;
        final var modifiedResult = modifiedFunction.apply(arrayInput);
        assertTrue(modifiedResult.isSuccess());
        final var modifiedValue = (Object[]) modifiedResult.getValue();
        Stream.of(modifiedValue).forEach( v ->
                assertEquals(v,value));
        final var modifiedFail = modifiedFunction.apply("NOT INPUT");
        assertTrue(modifiedFail.isFailure());
    }

    /*
     * GIVEN a field FI with type not an array with the annotation CsvArray with separator S
     *  AND a csvFieldModifier CFM
     * WHEN the method compute is invoked with FI
     * THEN the result is a failure
     */
    @Test
    public void testFail() throws NoSuchFieldException {
        class TypeT{}
        final var separator = "-";
        // GIVEN a field FI with type not an array with the annotation CsvArray with separator S
        class ModifierrSample {
            @SuppressWarnings("unused")
            @CsvArray(separator)
            private TypeT simpleData;
        }
        final var field = ModifierrSample.class.getDeclaredField("simpleData");
        // AND a csvFieldModifier CFM
        final var csvFieldModifier = CsvFieldModifier.getInstance();
        // WHEN the method compute is invoked with FI
        final var result = csvFieldModifier.compute(field);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        final var exception = result.getException();
        assertEquals(exception.getClass(), MissingArrayException.class);
        assertEquals(exception.getMessage(),String.format(MissingArrayException.messageFormatter, field.getType().toString(), field.getName()));

    }
}
