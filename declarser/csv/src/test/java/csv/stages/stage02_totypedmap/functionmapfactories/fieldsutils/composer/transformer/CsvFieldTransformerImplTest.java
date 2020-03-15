package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.CsvFieldComposer;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.Declarser;
import kernel.tryapi.Try;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldTransformerImplTest {

    @Test
    public void test(){

    }

    /*
     * GIVEN a field FI of type TypeT with the annotation CsvColumn with value K
     *  AND a value of type TypeT T
     *  AND an input string I
     *  AND an exception E
     *  AND a function F: String -> Try<?>  that map I -> Success(T) and other input into Fail(E)
     *  AND a map M containing the entry (TypeT.class, F)
     *  AND AND a CsvFieldTransformer CsvFT constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the function maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    @Test
    public void compute_automatically_a_valid_transformer_return_a_success() throws NoSuchFieldException {
        final var csvDeclarserFactory = Mockito.mock(CsvDeclarserFactory.class);
        final var csvPreValidatorsFactory = CsvPreValidatorsFactory.of(new HashMap<>(), new HashMap<>());
        final var csvPreValidatorsExtractor = CsvPreValidatorsExtractor.getInstance();
        // GIVEN a field FI of type TypeT
        class TypeT {}
        class ComposerSample {
            @SuppressWarnings("unused")
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a value of type TypeT T
        final var type = new TypeT();
        // AND an input string I
        final var input = "input";
        // AND an exception E
        final var fail = new Exception();
        // AND a function F: String -> Try<?>  that map I -> Success(T) and other input into Fail(E)
        final Function<String, Try<?>> function = t -> input.equals(t) ? Try.success(type) : Try.fail(fail);
        // AND a map M containing the entry (TypeT.class, F)
        final var autoFunctionClassMap = new HashMap<Class<?>, Function<String, Try<?>>>();
        autoFunctionClassMap.put(
                TypeT.class,
                function);
        // AND a CsvFieldTransformer CsvFT constructed with M
        final var transformer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                new HashMap<>(),
                autoFunctionClassMap);
        // WHEN the method compute is invoked with FI
        final var result = transformer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a function maps I into Success(T)
        final var functionResult = result.getValue();
        final var inputResult = functionResult.apply(input);
        assertTrue(inputResult.isSuccess());
        final var inputValue = inputResult.getValue();
        assertEquals(inputValue, type);
        // AND Fail(E) otherwise
        final var failResult = functionResult.apply("NOT"+input);
        assertTrue(failResult.isFailure());
        final var failValue = failResult.getException();
        assertEquals(failValue, fail);
    }
}
