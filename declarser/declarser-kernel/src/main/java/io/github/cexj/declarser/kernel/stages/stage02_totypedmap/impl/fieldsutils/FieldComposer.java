package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils;

import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer.Transformer;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;

public interface FieldComposer<K,T> {
    Try<Transformer<K,T>> compute(final Field field);
}
