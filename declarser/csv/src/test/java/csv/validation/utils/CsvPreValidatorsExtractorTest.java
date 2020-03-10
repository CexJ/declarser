package csv.validation.utils;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.CsvFieldsExtractor;
import csv.validation.utils.utils.ArgsValidatorClass1;
import csv.validation.utils.utils.ArgsValidatorClass2;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvPreValidatorsExtractorTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(CsvPreValidatorsExtractor.getInstance(), CsvPreValidatorsExtractor.getInstance());
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CsvPreValidatorsExtractor> constructor = CsvPreValidatorsExtractor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    final CsvPreValidation ann1 = new CsvPreValidation(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return CsvPreValidation.class;
        }
        @Override
        public Class<? extends Validator<String>> value() {
            return ArgsValidatorClass1.class;
        }
        @Override
        public String[] params() {
            return new String[]{"ann1"};
        }
    };

    final CsvPreValidation ann2 = new CsvPreValidation(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return CsvPreValidation.class;
        }
        @Override
        public Class<? extends Validator<String>> value() {
            return ArgsValidatorClass2.class;
        }
        @Override
        public String[] params() {
            return new String[]{"ann2"};
        }
    };


    /* GIVEN a list L of elements Vi(CLi, PRMi) of type CsvPreValidation
     *  AND a CsvPreValidatorsExtractor C
     * WHEN the method extract is invoked with L
     * THEN the result is a list of elements Vi(CLi, PRMi) of type PreValidation<String>
     */
    @Test
    public void extract_csvprevalidation_annotation_return_the_prevalidation_list_associated(){
        // GIVEN a list L of elements Vi(CLi, PRMi) of type CsvPreValidation
        final var inputList = Arrays.asList(ann1, ann2);
        // AND a CsvPreValidatorsExtractor C
        final var extractor = CsvPreValidatorsExtractor.getInstance();
        // WHEN the method extract is invoked with L
        final var result = extractor.extract(inputList);
        // THEN the result is a list of elements Vi(CLi, PRMi) of type PreValidation<String>
        assertEquals(result.size(),2);
        assertEquals(result.get(0).getClazz(),ann1.value());
        assertEquals(result.get(0).getParams()[0],ann1.params()[0]);
        assertEquals(result.get(1).getClazz(),ann2.value());
        assertEquals(result.get(1).getParams()[0],ann2.params()[0]);


    }
}
