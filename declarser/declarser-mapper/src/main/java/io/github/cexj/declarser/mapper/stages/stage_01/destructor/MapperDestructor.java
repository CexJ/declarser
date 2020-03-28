package io.github.cexj.declarser.mapper.stages.stage_01.destructor;

import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.destructor.Destructor;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public interface MapperDestructor<I> extends Destructor<I, String, Try<?>> {


    static <I> MapperDestructor<I> of(
            final Class<I> fromClazz,
            final Map<String, Function<I, Try<?>>> fieldFunctionMap) {
        return MapperDestructorImpl.of(fromClazz, fieldFunctionMap);
    }

    @Override
    Try<Map<String, Try<?>>> destruct(final I input);
}
