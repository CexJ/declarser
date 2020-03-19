package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import kernel.exceptions.GroupedException;
import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldsExtractor;
import kernel.stages.stage02_totypedmap.impl.trasformer.Transformer;
import kernel.tryapi.Try;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFunctionMapFactoryImplTest {

    private static class TypeA {}
    private static class DataSample {
        @SuppressWarnings("unused")
        private TypeA valueA;
    }

    private static FieldComposer<Integer, String> functionComposer;
    private static FieldsExtractor fieldsExtractor;
    private static Set<Field> fields;
    private static Transformer<Integer, String> transformer;
    private static int key;
    private static Function<String, Try<?>> function;

    /*
     * LET FC be a FieldComposer
     * AND FE be a FieldsExtractor
     * AND FIS be a set of fields
     * AND F be a function Function<String, Try<?>>
     * AND K be an integer
     * AND T be a Transformer constructed with K and F
     */
    @SuppressWarnings("unchecked")
    @BeforeAll
    public static void init(){
        functionComposer = Mockito.mock(FieldComposer.class);
        fieldsExtractor = Mockito.mock(FieldsExtractor.class);
        fields = new HashSet<>(Arrays.asList(DataSample.class.getDeclaredFields()));
        function = s -> Try.success("OK");
        key = 0;
        transformer = Transformer.of(key, function);
    }

    /*
     * GIVEN FE that returns FIS
     *  AND FC that returns T
     *  AND a CsvFunctionMapFactory CFMF constructed with FE and FC
     * WHEN the mapColumnToTransformer method is invoked
     * THEN the result is a success
     *  AND the map consists in the entry (K,F)
     */
    @Test
    public void map_valid_class_return_success(){
        // GIVEN FE that returns FIS
        Mockito.when(fieldsExtractor.extract(Mockito.any())).thenReturn(fields);
        // AND FC that returns T
        Mockito.when(functionComposer.compute(Mockito.any())).thenReturn(Try.success(transformer));
        // AND a CsvFunctionMapFactory CFMF constructed with FE and FC
        final var csvFunctionMapFactory = CsvFunctionMapFactory.of(fieldsExtractor,functionComposer);
        // WHEN the mapColumnToTransformer method is invoked
        final var result = csvFunctionMapFactory.mapColumnToTransformer(DataSample.class);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the map consists in the entry (K,F)
        final var value = result.getValue();
        assertEquals(value.size(), 1);
        value.forEach((k,v) -> {
                assertEquals(k, key);
                assertEquals(v, function);});
    }

    /*
     * GIVEN an exception E
     *  AND FE that returns FIS
     *  AND FC that returns Failure(E)
     *  AND a CsvFunctionMapFactory CFMF constructed with FE and FC
     * WHEN the mapColumnToTransformer method is invoked
     * THEN the result is a failure
     *  AND the exception is of type GroupedException
     */
    @Test
    public void map_invalid_class_return_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND FE that returns FIS
        Mockito.when(fieldsExtractor.extract(Mockito.any())).thenReturn(fields);
        // AND FC that returns Failure(E)
        Mockito.when(functionComposer.compute(Mockito.any())).thenReturn(Try.fail(exception));
        // AND a CsvFunctionMapFactory CFMF constructed with FE and FC
        final var csvFunctionMapFactory = CsvFunctionMapFactory.of(fieldsExtractor,functionComposer);
        // WHEN the mapColumnToTransformer method is invoked
        final var result = csvFunctionMapFactory.mapColumnToTransformer(DataSample.class);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of type GroupedException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), GroupedException.class);
    }
}
