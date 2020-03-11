package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils;

import csv.CsvDeclarserFactory;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.sample.ComposerSample;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.Declarser;
import kernel.tryapi.Try;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CsvFieldComposerTest {

    private final static class TypeO{}

    @Test
    public void compute_automatically_a_valid_transformer_return_a_success() throws NoSuchFieldException {
        final CsvDeclarserFactory csvDeclarserFactory = new CsvDeclarserFactory() {
            @Override
            public <O> Try<Declarser<String, Integer, String, O>> declarserOf(Class<O> clazz, Validator<O> postValidator, String cellSeparator) {
                return Try.fail(new Exception());
            }
        };
        CsvPreValidatorsFactory csvPreValidatorsFactory = CsvPreValidatorsFactory.of(new HashMap<>(), new HashMap<>());
        CsvPreValidatorsExtractor csvPreValidatorsExtractor = CsvPreValidatorsExtractor.getInstance();
        Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap = new HashMap<>();
        autoFunctionClassMap.put(
                String.class,
                s -> Try.success(s));
        final var composer = CsvFieldComposer.of(
                csvDeclarserFactory,
                csvPreValidatorsFactory,
                csvPreValidatorsExtractor,
                new HashMap<>(),
                autoFunctionClassMap);
        composer.compute(ComposerSample.class.getDeclaredField("simpleData") );
    }
}
