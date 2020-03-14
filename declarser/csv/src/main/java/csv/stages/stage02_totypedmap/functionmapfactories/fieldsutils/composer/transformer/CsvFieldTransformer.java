package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import csv.CsvDeclarserFactory;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public interface CsvFieldTransformer {

    static CsvFieldTransformer of(
            CsvDeclarserFactory csvDeclarserFactory,
            CsvPreValidatorsFactory preValidatorFactory,
            CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        return CsvFieldTransformerImpl.of(
                csvDeclarserFactory,
                preValidatorFactory,
                csvPreValidatorsExtractor,
                functionClassMap,
                autoFunctionClassMap);
    }

    Try<Function<String, Try<?>>> compute(Field field);
}
