package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import csv.stages.annotations.prevalidations.CsvPreValidation;
import csv.stages.annotations.prevalidations.CsvPreValidations;
import csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import csv.validation.utils.factory.CsvPreValidatorsFactory;
import kernel.tryapi.Try;
import kernel.validations.Validator;
import kernel.validations.prevalidations.PreValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldPrevalidatorImplTest {

    static class ValidatorMock implements Validator<String>{
        @Override
        public Optional<? extends Exception> apply(String s) {
            return Optional.empty();
        }
    }


    /*
     * GIVEN a field FI with
             the annotation CsvPreValidations with value a list of Validators VS
     *  AND a string I
     *  AND an exception E
     *  AND a validator V that maps I into Optional.empty and Optional.of(Exception) the rest
     *  AND a CsvPreValidatorsFactory CPVF such that it returns V when function is invoked
     *  AND a prevalidator PV
     *  AND a CsvPreValidatorsExtractor CPVE such that it returns V when function is invoked
     *  AND a CsvFieldPrevalidator CFPV constructed with CPVF and CPVE
     * WHEN the method compute is invoked with FI
     * THEN the result is a success
     *  AND the validator is V
     *
     */
    @Test
    public void test() throws NoSuchFieldException {

        // GIVEN a field FI with
        //       the annotation CsvPreValidations with value a list of Validators VS
        class TypeT{}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvPreValidations({
                    @CsvPreValidation(ValidatorMock.class),
                    @CsvPreValidation(ValidatorMock.class)})
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        // AND a string I
        final var input = "input";
        // AND an exception E
        final var exception = new Exception();
        // AND a validator V that maps I into Optional.empty and Optional.of(Exception) the rest
        final Validator<String> validator = s -> input.equals(s) ? Optional.empty() : Optional.of(exception);
        // AND a CsvPreValidatorsFactory CPVF such that it returns V when function is invoked
        final var csvPreValidatorsFactory = Mockito.mock(CsvPreValidatorsFactory.class);
        Mockito.when(csvPreValidatorsFactory.function(Mockito.any())).thenReturn(Try.success(validator));
        // AND a prevalidator PV
        final var preValidator = PreValidator.of(ValidatorMock.class, new String[]{});
        // AND a CsvPreValidatorsExtractor CPVE such that it returns V when function is invoked
        final var csvPreValidatorsExtractor = Mockito.mock(CsvPreValidatorsExtractor.class);
        Mockito.when(csvPreValidatorsExtractor.extract(Mockito.any())).thenReturn(preValidator);
        // AND a CsvFieldPrevalidator CFPV constructed with CPVF and CPVE
        final var csvFieldPrevalidator = CsvFieldPrevalidator.of(
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor);
        // WHEN the method compute is invoked with FI
        final var result = csvFieldPrevalidator.compute(field);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the validator is V
        final var value = result.getValue();
        assertTrue(value.apply(input).isEmpty());
        final var error = value.apply("NOT"+input);
        assertTrue(error.isPresent());
        assertEquals(error.get(), exception);

    }
}
