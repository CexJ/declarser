package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvArray;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.annotations.fields.CsvField;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.CsvFieldComposer;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingTransformerException;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.Declarser;
import kernel.exceptions.GroupedException;
import kernel.tryapi.Try;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldComposerTest {

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
     * THEN the result is a success
     *  AND the value is a Transformer with key equals to K
     *  AND function FR such that
     *  AND FR maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    @Test
    public void compute_automatically_a_valid_transformer_return_a_success() throws NoSuchFieldException {
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
        // AND a field FI of type TypeT with the annotation CsvColumn with value K
        class TypeT {}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvColumn(key)
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
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a Transformer with key equals to K
        final var value = result.getValue();
        assertEquals(Integer.valueOf(key), value.getKey());
        // AND function FR such that
        final var functionResult = value.getFunction();
        // AND FR maps I into Success(T)
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
     *  AND a CsvFieldComposer CsvFC constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the value is a Transformer with key equals to K
     *  AND function FR such that
     *  AND FR maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    @Test
    public void compute_with_a_custom_transformer_return_a_success() throws NoSuchFieldException {
        final var csvDeclarserFactory = new CsvDeclarserFactory() {
            @Override
            public <O> Try<Declarser<String, Integer, String, O>> declarserOf(Class<O> clazz, Validator<O> postValidator, String cellSeparator) {
                return Try.fail(new Exception());
            }
        };
        final var csvPreValidatorsFactory = CsvPreValidatorsFactory.of(new HashMap<>(), new HashMap<>());
        final var csvPreValidatorsExtractor = CsvPreValidatorsExtractor.getInstance();

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
        // AND an integer K
        final int key = 0;
        // AND a field FI of type TypeT with
        //      the annotation CsvColumn with value K
        //      the annotation CsvField with value TTransformer and {I} as params
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvColumn(key)
            @CsvField(value=TTransformer.class, params = {input})
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a map M containing the entry (TTransformer.class, C)
        final var autoFunctionClassMap = new HashMap<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>>();
        autoFunctionClassMap.put(
                TTransformer.class,
                constructor);
        // AND a CsvFieldComposer CsvFC constructed with M
        final var composer = CsvFieldComposer.of(
                csvDeclarserFactory,
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor,
                autoFunctionClassMap,
                new HashMap<>());
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a Transformer with key equals to K
        final var value = result.getValue();
        assertEquals(Integer.valueOf(key), value.getKey());
        // AND function FR such that
        final var functionResult = value.getFunction();
        // AND FR maps I into Success(T)
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
     * GIVEN an integer K
     *  AND a field FI of type TypeT with
            the annotation CsvColumn with value K
     *  AND a CsvFieldComposer CsvFC
     * WHEN the method compute is invoked with FI
     * THEN the result is a failure
     *  AND the exception is of type MissingTransformerException
     *  AND the message is formatted with FI
     */
    @Test
    public void compute_with_a_invalid_transformer_return_a_failure() throws NoSuchFieldException {
        final var csvDeclarserFactory = new CsvDeclarserFactory() {
            @Override
            public <O> Try<Declarser<String, Integer, String, O>> declarserOf(Class<O> clazz, Validator<O> postValidator, String cellSeparator) {
                return Try.fail(new Exception());
            }
        };
        final var csvPreValidatorsFactory = CsvPreValidatorsFactory.of(new HashMap<>(), new HashMap<>());
        final var csvPreValidatorsExtractor = CsvPreValidatorsExtractor.getInstance();

        class TypeT {}

        // GIVEN an integer K
        final int key = 0;
        // AND a field FI of type TypeT with
        //      the annotation CsvColumn with value K
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvColumn(key)
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a CsvFieldComposer CsvFC constructed
        final var composer = CsvFieldComposer.of(
                csvDeclarserFactory,
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor,
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

    /*
     * GIVEN an integer K
     *  AND a separator S
     *  AND a field FI of type TypeT
            with the annotation CsvColumn with value K
            and the annotation CsvArray with separator S
     *  AND a value of type TypeT T
     *  AND an input string I
     *  AND an exception E
     *  AND a function F: String -> Try<?>  that map I -> Success(T) and other input into Fail(E)
     *  AND a map M containing the entry (TypeT.class, F)
     *  AND a CsvFieldComposer CsvFC constructed with M
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the value is a Transformer with key equals to K
     *  AND function FR such that
     *  AND FR maps I into Success(T)
     *  AND Fail(E) otherwise
     */
    @Test
    public void compute_array_transformer_return_a_success() throws NoSuchFieldException {
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
        // AND a separator S
        final var separator = "-";
        // AND a field FI of type TypeT
        //      with the annotation CsvColumn with value K
        //      and the annotation CsvArray with separator S
        class TypeT {}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvColumn(key)
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
        // AND a CsvFieldComposer CsvFC constructed with M
        final var composer = CsvFieldComposer.of(
                csvDeclarserFactory,
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor,
                new HashMap<>(),
                autoFunctionClassMap);
        // WHEN the method compute is invoked with FI
        final var result = composer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a Transformer with key equals to K
        final var value = result.getValue();
        assertEquals(Integer.valueOf(key), value.getKey());
        // AND function FR such that
        final var functionResult = value.getFunction();
        // AND FR maps I+separator+I into Success(T)
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
    @Test
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
