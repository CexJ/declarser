package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import csv.stages.annotations.validations.pre.CsvPreValidations;
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


    @Test
    public void test() throws NoSuchFieldException {

        class TypeT{}
        class ComposerSample {
            @SuppressWarnings("unused")
            @CsvPreValidations({
                    @CsvPreValidation(ValidatorMock.class),
                    @CsvPreValidation(ValidatorMock.class)})
            private TypeT simpleData;
        }
        final var field = ComposerSample.class.getDeclaredField("simpleData");
        final var input = "input";
        final var exception = new Exception();
        final Validator<String> validator = s -> input.equals(s) ? Optional.empty() : Optional.of(exception);
        final var csvPreValidatorsFactory = Mockito.mock(CsvPreValidatorsFactory.class);
        Mockito.when(csvPreValidatorsFactory.function(Mockito.any())).thenReturn(Try.success(validator));
        final var csvPreValidatorsExtractor = Mockito.mock(CsvPreValidatorsExtractor.class);
        final var preValidator = PreValidator.of(ValidatorMock.class, new String[]{});
        Mockito.when(csvPreValidatorsExtractor.extract(Mockito.any())).thenReturn(preValidator);
        final var csvFieldPrevalidator = CsvFieldPrevalidator.of(
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor);
        final var result = csvFieldPrevalidator.compute(field);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertTrue(value.apply(input).isEmpty());
        final var error = value.apply("NOT"+input);
        assertTrue(error.isPresent());
        assertEquals(error.get(), exception);

    }
}
