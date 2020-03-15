package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvField;
import csv.stages.annotations.fields.CsvNode;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import kernel.Declarser;
import kernel.tryapi.Try;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldTransformerImplTest {

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

    /*
     * GIVEN a value of type TypeT T
     *  AND an input string I
     *  AND an exception E
     *  AND a transformer TTransformer with
            a constructor that initialize a String field input
            and an apply method that map input -> Success(T) and other input into Fail(E)
     *  AND a function C that construct an object TTransformer from an array of string
     *  AND an integer K
     *  AND a field FI of type TypeT with
            the annotation CsvColumn with value K
            the annotation CsvField with value TTransformer and {I} as params
     *  AND a map M containing the entry (TTransformer.class, C)
     *  AND a CsvFieldTransformer CsvFT constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the function maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    @Test
    public void compute_with_a_custom_transformer_return_a_success() throws NoSuchFieldException {
        final var csvDeclarserFactory = Mockito.mock(CsvDeclarserFactory.class);
        class TypeT {}
        // GIVEN a value of type TypeT T
        final var type = new TypeT();
        // AND an input string I
        final var input = "input";
        // AND an exception E
        final var fail = new Exception();
        // AND a transformer TTransformer with
        //      a constructor that initialize a String field input
        //      and an apply method that map input -> Success(T) and other input into Fail(E)
        class TTransformer implements Function<String, Try<?>>{
            private final String input;
            public TTransformer(String input){
                this.input = input;
            }
            @Override
            public Try<TypeT> apply(String s) {
                return input.equals(s) ? Try.success(type) : Try.fail(fail);
            }
        }
        // AND a function C that construct an object TTransformer from an array of string
        final Function<String[], Function<String, Try<?>>> constructor = ss -> new TTransformer(ss[0]);
        // AND a field FI of type TypeT with
        //      the annotation CsvField with value TTransformer and {I} as params
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvField(value=TTransformer.class, params = {input})
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a map M containing the entry (TTransformer.class, C)
        final var autoFunctionClassMap = new HashMap<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>>();
        autoFunctionClassMap.put(
                TTransformer.class,
                constructor);
        // AND a CsvFieldTransformer CsvFT constructed with M
        final var composer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                autoFunctionClassMap,
                new HashMap<>());
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the function  maps I into Success(T)
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

    /*
     * GIVEN a value of type TypeT T
     *  AND an input string I
     *  AND a separator S
     *  AND a DeclarserFactory DF that can parse TypeT with separator S
     *  AND a field FI of type TypeT with
            the annotation CsvNode and separator S
     *  AND a CsvFieldTransformer CsvFT constructed with DF
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the function maps I into Success(T)
     */
    @Test
    public void compute_with_a_node_transformer_return_a_success() throws NoSuchFieldException {
        class TypeT {}
        // GIVEN a value of type TypeT T
        final var type = new TypeT();
        // AND an input string I
        final var input = "input";
        // AND a separator S
        final var separator = "*";
        // AND a DeclarserFactory DF that can parse TypeT with separator S
        final var csvDeclarserFactory = Mockito.mock(CsvDeclarserFactory.class);
        final var declarser = Mockito.mock(Declarser.class);
        //noinspection unchecked
        Mockito.when(csvDeclarserFactory.declarserOf(Mockito.any(), Mockito.any()))
                .thenReturn(Try.success(declarser));
        //noinspection unchecked
        Mockito.when(declarser.parse(Mockito.any()))
                .thenReturn(Try.success(type));
        // AND a field FI of type TypeT with
        //      the annotation CsvNode and separator S
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvNode(separator)
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a CsvFieldTransformer CsvFT constructed with DF
        final var composer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                new HashMap<>(),
                new HashMap<>());
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the function  maps I into Success(T)
        final var functionResult = result.getValue();
        final var inputResult = functionResult.apply(input);
        assertTrue(inputResult.isSuccess());
        final var inputValue = inputResult.getValue();
        assertEquals(inputValue, type);
    }

    /*
   * GIVEN a field FI of type TypeT
   *  AND a CsvFieldTransformer CsvFT
   * WHEN the method compute is invoked with FI
   * THEN the result is a failure
   *  AND the exception is of type MissingTransformerException
   *  AND the message is formatted with FI
   */
    @Test
    public void compute_with_a_invalid_transformer_return_a_failure() throws NoSuchFieldException {
        final var csvDeclarserFactory = Mockito.mock(CsvDeclarserFactory.class);
        class TypeT {}
        // GIVEN a field FI of type TypeT
        class ComposerSample {
            @SuppressWarnings("unused")
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a CsvFieldTransformer CsvFT constructed
        final var composer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                new HashMap<>(),
                new HashMap<>());
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of type MissingTransformerException
        final var exception = result.getException();
        assertEquals(exception.getClass(), MissingTransformerException.class);
        // AND the message is formatted with FI
        assertEquals(exception.getMessage(), String.format(MissingTransformerException.messageFormatter, field.getName()));
    }


}
