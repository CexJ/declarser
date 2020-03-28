package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier.CsvFieldModifier;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator.CsvFieldPrevalidator;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldComposerTest {

    static class TypeT{}
    static final int key = 0;
    static class ComposerSample {
        @SuppressWarnings("unused")
        @CsvColumn(key)
        private TypeT simpleData;
    }

    private static Field field;
    private static TypeT value;
    private static String input;
    private static String  input1;
    private static String  input2;
    private static String  input3;
    private static Exception exception1 = new Exception();
    private static Exception exception2 = new Exception();
    private static Exception exception3 = new Exception();
    private static UnaryOperator<Parser<String,?>> modifier;
    private static Validator<String> validator;
    private static Parser<String,?> function;
    private static CsvFieldModifier csvFieldModifier;
    private static CsvFieldPrevalidator csvFieldPrevalidator;
    private static CsvFieldTransformer csvFieldTransformer;

    /*
     * LET F be a field with CsvColumn with value K
     * AND T be a value of type TypeT
     * AND I be a string
     * AND I1 be a string
     * AND I2 be a string
     * AND I3 be a string
     * AND E1 be an exception
     * AND E2 be an exception
     * AND E3 be an exception
     * AND M be a UnaryOperator<Function<String, Try<?>>> such that
     *     it maps G into a map such that I1 -> Fail(E1) and G for the rest
     * AND V be a Validator<String> such that
     *     I2 -> Optional.of(E2) and Optional.empty for the rest
     * AND F be a Function<String, Try<?>> function such that
     *     I3 -> Failure(E3) and Success(T) for the rest
     * AND CFM be a CsvFieldModifier
     * AND CFP be a CsvFieldPrevalidator
     * AND CFT be a CsvFieldTransformer
     */
    @SuppressWarnings("unchecked")
    @BeforeAll
    public static void init() throws NoSuchFieldException {
        // LET F be a field with CsvColumn with value K
        field = ComposerSample.class.getDeclaredField("simpleData");
        // AND T be a value of type TypeT
        value = new TypeT();
        // AND I be a string
        input = "input";
        // AND I1 be a string
        input1 = "input1";
        // AND I2 be a string
        input2 = "input2";
        // AND I3 be a string
        input3 = "input3";
        // AND E1 be an exception
        exception1 = new Exception();
        // AND E2 be an exception
        exception2 = new Exception();
        // AND E3 be an exception
        exception3 = new Exception();
        // AND M be a UnaryOperator<Function<String, Try<?>>> such that
        //     it maps G into a map such that I1 -> Fail(E1) and G for the rest
        modifier = f ->  s -> input1.equals(s) ? Try.fail(exception1) : (Try<Object>) f.apply(s);
        // AND V be a Validator<String> such that
        //     I2 -> Optional.of(E2) and Optional.empty for the rest
        validator = s -> input2.equals(s) ? Optional.of(exception2) : Optional.empty();
        // AND F be a Function<String, Try<?>> function such that
        //     I3 -> Failure(E3) and Success(T) for the rest
        function = s -> input3.equals(s) ? Try.fail(exception3) : Try.success(value);
        // AND CFM be a CsvFieldModifier
        csvFieldModifier = Mockito.mock(CsvFieldModifier.class);
        // AND CFP be a CsvFieldPrevalidator
        csvFieldPrevalidator = Mockito.mock(CsvFieldPrevalidator.class);
        // AND CFT be a CsvFieldTransformer
        csvFieldTransformer = Mockito.mock(CsvFieldTransformer.class);
    }


    /*
     * GIVEN that CFM.compute(FI) return Success(M)
     *  AND that CFP.compute(FI) return Sucess(V)
     *  AND that CFT.compute(FI) return Sucess(F)
     *  AND a CsvFieldComposer C constructed with CFM, CFP, and CFT
     * WHEN the method compute is invokec with FI
     * THEN the result is a success
     *  AND the transformer has key equals to K
     *  AND its function maps
     *      I1 -> Failure(E1)
     *      I2 -> Failure(E2)
     *      I3 -> Failure(E3)
     *      I -> Success(T)
     */
    @Test
    public void compute_valid_field_return_success(){
        // GIVEN that CFM.compute(FI) return Success(M)
        Mockito.when(csvFieldModifier.compute(Mockito.any())).thenReturn(Try.success(modifier));
        // AND that CFP.compute(FI) return Sucess(V)
        Mockito.when(csvFieldPrevalidator.compute(Mockito.any())).thenReturn(Try.success(validator));
        // AND that CFT.compute(FI) return Sucess(F)
        Mockito.when(csvFieldTransformer.compute(Mockito.any())).thenReturn(Try.success(function));
        // AND a CsvFieldComposer C constructed with CFM, CFP, and CFT
        final var csvFieldComposer = CsvFieldComposer.of(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
        // WHEN the method compute is invokec with FI
        final var result = csvFieldComposer.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the transformer has key equals to K
        final var transformer = result.getValue();
        assertEquals(transformer.getKey(), key);
        // AND its function maps
        //     I1 -> Failure(E1)
        final var transformerFunciton = transformer.getParser();
        final var output1 = transformerFunciton.apply(input1);
        assertTrue(output1.isFailure());
        assertEquals(output1.getException(), exception1);
        //     I2 -> Failure(E2)
        final var output2 = transformerFunciton.apply(input2);
        assertTrue(output2.isFailure());
        assertEquals(output2.getException(), exception2);
        //     I3 -> Failure(E3)
        final var output3 = transformerFunciton.apply(input3);
        assertTrue(output3.isFailure());
        assertEquals(output3.getException(), exception3);
        //     I -> Success(T)
        final var output = transformerFunciton.apply(input);
        assertTrue(output.isSuccess());
        assertEquals(output.getValue(), value);
    }

    /*
     * GIVEN an exception E
     *  AND that CFM.compute(FI) return Failure(E)
     *  AND that CFP.compute(FI) return Sucess(V)
     *  AND that CFT.compute(FI) return Sucess(F)
     *  AND a CsvFieldComposer C constructed with CFM, CFP, and CFT
     * WHEN the method compute is invokec with FI
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void compute_failing_modifier_return_failure(){
        final var exceptionModifier = new Exception();
        Mockito.when(csvFieldModifier.compute(Mockito.any())).thenReturn(Try.fail(exceptionModifier));
        Mockito.when(csvFieldPrevalidator.compute(Mockito.any())).thenReturn(Try.success(validator));
        Mockito.when(csvFieldTransformer.compute(Mockito.any())).thenReturn(Try.success(function));
        final var csvFieldComposer = CsvFieldComposer.of(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
        final var result = csvFieldComposer.compute(field);
        assertTrue(result.isFailure());
        final var exception = result.getException();
        assertEquals(exception, exceptionModifier);
    }

    /*
     * GIVEN an exception E
     *  AND that CFM.compute(FI) return Success(M)
     *  AND that CFP.compute(FI) return Failure(E)
     *  AND that CFT.compute(FI) return Sucess(F)
     *  AND a CsvFieldComposer C constructed with CFM, CFP, and CFT
     * WHEN the method compute is invokec with FI
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void compute_failing_prevalidation_return_failure(){
        final var exceptionPrevalidator = new Exception();
        Mockito.when(csvFieldModifier.compute(Mockito.any())).thenReturn(Try.success(modifier));
        Mockito.when(csvFieldPrevalidator.compute(Mockito.any())).thenReturn(Try.fail(exceptionPrevalidator));
        Mockito.when(csvFieldTransformer.compute(Mockito.any())).thenReturn(Try.success(function));
        final var csvFieldComposer = CsvFieldComposer.of(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
        final var result = csvFieldComposer.compute(field);
        assertTrue(result.isFailure());
        final var exception = result.getException();
        assertEquals(exception, exceptionPrevalidator);
    }

    /*
     * GIVEN an exception E
     *  AND that CFM.compute(FI) return Success(M)
     *  AND that CFP.compute(FI) return Sucess(V)
     *  AND that CFT.compute(FI) return Failure(E)
     *  AND a CsvFieldComposer C constructed with CFM, CFP, and CFT
     * WHEN the method compute is invokec with FI
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void compute_failing_transformer_return_failure(){
        final var exceptionTransformer = new Exception();
        Mockito.when(csvFieldModifier.compute(Mockito.any())).thenReturn(Try.success(modifier));
        Mockito.when(csvFieldPrevalidator.compute(Mockito.any())).thenReturn(Try.success(validator));
        Mockito.when(csvFieldTransformer.compute(Mockito.any())).thenReturn(Try.fail(exceptionTransformer));
        final var csvFieldComposer = CsvFieldComposer.of(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
        final var result = csvFieldComposer.compute(field);
        assertTrue(result.isFailure());
        final var exception = result.getException();
        assertEquals(exception, exceptionTransformer);
    }

}
