package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public interface CsvFieldTransformer {

    static CsvFieldTransformer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        return CsvFieldTransformerImpl.of(
                csvDeclarserFactory,
                functionClassMap,
                autoFunctionClassMap);
    }

    Try<Function<String, Try<?>>> compute(Field field);
}
