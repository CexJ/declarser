package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvArray;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.CsvFieldComposer;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.Declarser;
import kernel.exceptions.GroupedException;
import kernel.tryapi.Try;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldModifierImplTest {


    /*
     * GIVEN a separator S
     *  AND a field FI of type TypeT
            with the annotation CsvArray with separator S
     *  AND a value of type TypeT T
     *  AND an input string I
     *  AND an exception E
     *  AND a function F: String -> Try<?>  that map I -> Success(T) and other input into Fail(E)
     *  AND a map M containing the entry (TypeT.class, F)
     *  AND a CsvFieldTransformer CsvFT constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the function maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    public void compute_array_transformer_return_a_success() throws NoSuchFieldException {
        final var csvDeclarserFactory = Mockito.mock(CsvDeclarserFactory.class);
        // GIVEN a separator S
        final var separator = "-";
        // AND a field FI of type TypeT
        //      with the annotation CsvArray with separator S
        class TypeT {}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvArray(separator)
            private TypeT[] simpleData;
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
        // AND function maps I+separator+I into Success(T)
        final var functionResult = result.getValue();
        final var inputResult = functionResult.apply(input+separator+input);
        assertTrue(inputResult.isSuccess());
        System.out.println(inputResult.getValue().getClass());
        final Object[] inputValue = (Object[]) (inputResult.getValue());
        System.out.println(inputValue[0]);
        assertEquals(inputValue[0], type);
        assertEquals(inputValue[1], type);
        // AND Fail(E) otherwise
        final var failResult = functionResult.apply("NOT"+input);
        assertTrue(failResult.isFailure());
        final var failValue = failResult.getException();
        assertEquals(failValue.getClass(), GroupedException.class);
        final var failValues = ((GroupedException) failValue).getExceptions();
        assertEquals(failValues.size(), 1);
        failValues.forEach( f ->
                assertEquals(f, fail) );
    }

    /*
     * GIVEN an integer K
     *  AND a field FI of type TypeT with the annotation CsvColumn with value K
     *  AND a value of type TypeT T
     *  AND an input string I
     *  AND an exception E
     *  AND a function F: String -> Try<?>  that map I -> Success(T) and other input into Fail(E)
     *  AND a map M containing the entry (TypeT.class, F)
     *  AND a CsvFieldComposer CsvFC constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a failure
     *  AND the exception is of type MissingArrayException
     *  AND the message is formatted with FI
     */
    public void compute_array_transformer_for_not_an_array_field_return_a_failure() throws NoSuchFieldException {
        final var csvDeclarserFactory = new CsvDeclarserFactory() {
            @Override
            public <O> Try<Declarser<String, Integer, String, O>> declarserOf(Class<O> clazz, Validator<O> postValidator, String cellSeparator) {
                return Try.fail(new Exception());
            }
        };
        final var csvPreValidatorsFactory = CsvPreValidatorsFactory.of(new HashMap<>(), new HashMap<>());
        final var csvPreValidatorsExtractor = CsvPreValidatorsExtractor.getInstance();

        // GIVEN an integer K
        final int key = 0;

        final var separator = "-";
        // AND a field FI of type TypeT with the annotation CsvColumn with value K
        class TypeT {}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvColumn(key)
            @CsvArray(separator)
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
        // AND a CsvFieldComposer CsvFC constructed with M
        final var composer = CsvFieldComposer.of(
                csvDeclarserFactory,
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor,
                new HashMap<>(),
                autoFunctionClassMap);
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of type MissingArrayException
        final var exception = result.getException();
        assertEquals(exception.getClass(), MissingArrayException.class);
        // AND the message is formatted with FI
        assertEquals(exception.getMessage(), String.format(MissingArrayException.messageFormatter, field.getName()));
    }
}
