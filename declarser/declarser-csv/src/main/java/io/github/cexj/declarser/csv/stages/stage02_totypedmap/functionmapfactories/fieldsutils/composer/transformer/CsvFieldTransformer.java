package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public interface CsvFieldTransformer {

    static CsvFieldTransformer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final Map<Class<? extends Parser<String>>, Function<String[], Parser<String>>> functionClassMap,
            final Map<Class<?>, Parser<String>> autoFunctionClassMap) {
        return CsvFieldTransformerImpl.of(
                csvDeclarserFactory,
                functionClassMap,
                autoFunctionClassMap);
    }

    Try<Parser<String>> compute(Field field);
}
