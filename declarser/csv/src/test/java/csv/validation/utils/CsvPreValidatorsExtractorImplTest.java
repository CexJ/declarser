package csv.validation.utils;

import csv.stages.annotations.prevalidations.CsvPreValidation;
import csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import csv.validation.utils.utils.ArgsValidatorClass1;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class CsvPreValidatorsExtractorImplTest {

    @Test
    public void testFlyWeightPattern(){
        assertEquals(CsvPreValidatorsExtractor.getInstance(), CsvPreValidatorsExtractor.getInstance());
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


    /* GIVEN a list L of elements Vi(CLi, PRMi) of type CsvPreValidation
     *  AND a CsvPreValidatorsExtractor C
     * WHEN the method extract is invoked with L
     * THEN the result is a list of elements Vi(CLi, PRMi) of type PreValidation<String>
     */
    @Test
    public void extract_csvprevalidation_annotation_return_the_prevalidation_list_associated(){
        // GIVEN an element of type CsvPreValidation V
        // AND a CsvPreValidatorsExtractor C
        final var extractor = CsvPreValidatorsExtractor.getInstance();
        // WHEN the method extract is invoked with V
        final var result = extractor.extract(ann1);
        // THEN the result is a element V(CL, PRM) of type PreValidation<String>
        assertEquals(result.getClazz(),ann1.value());
        assertEquals(result.getParams()[0],ann1.params()[0]);
    }
}
