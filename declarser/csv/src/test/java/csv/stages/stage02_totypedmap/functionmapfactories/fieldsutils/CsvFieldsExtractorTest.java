package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.sample.DataSample;
import kernel.parsers.fromstring.tonumber.BigDecimalParser;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFieldsExtractorTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(CsvFieldsExtractor.getInstance(), CsvFieldsExtractor.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CsvFieldsExtractor> constructor = CsvFieldsExtractor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /*
     * GIVEN a class C
     *  AND a CsvExtractor E
     * WHEN the extract is invoked with C
     * THEN the result is the set of fields annotated by CsvColumn
     */
    @Test
    public void extract_from_a_class_return_the_fields_annotated_by_CsvColumn(){
        // GIVEN a class C
        final var clazz = DataSample.class;
        // AND a CsvExtractor E
        final var extractor = CsvFieldsExtractor.getInstance();
        // WHEN the extract is invoked with C
        final var result = extractor.extract(clazz);
        // THEN the result is the set of fields annotated by CsvColumn
        assertEquals(2, result.size());
        final var names = new HashSet<>(Arrays.asList("age", "name"));
        final var resultNames = result.stream().map(Field::getName).collect(Collectors.toSet());
        assertEquals(resultNames, names);
        result.forEach(field -> assertNotNull(field.getAnnotation(CsvColumn.class)));
        Stream.of(clazz.getDeclaredFields())
                .filter(field -> ! result.contains(field))
                .forEach( field -> assertNull(field.getAnnotation(CsvColumn.class)));
    }
}
