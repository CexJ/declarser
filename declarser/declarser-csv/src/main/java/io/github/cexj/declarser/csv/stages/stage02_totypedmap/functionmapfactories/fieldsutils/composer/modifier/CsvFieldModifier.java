package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

public interface CsvFieldModifier {

    static CsvFieldModifierImpl getInstance() {
        return CsvFieldModifierImpl.getInstance();
    }

    Try<UnaryOperator<Parser<String,?>>> compute(Field field);
}
