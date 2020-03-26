package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldsExtractor;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public interface CsvFunctionMapFactory {
    Try<Map<Integer, Parser<String>>> mapColumnToTransformer(
            Class<?> clazz);

    static CsvFunctionMapFactory of(
            final FieldsExtractor fieldsExtractor,
            final FieldComposer<Integer, String> functionComposer) {
        return CsvFunctionMapFactoryImpl.of(fieldsExtractor,functionComposer);
    }
}
