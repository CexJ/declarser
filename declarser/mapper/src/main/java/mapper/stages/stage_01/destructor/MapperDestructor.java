package mapper.stages.stage_01.destructor;

import kernel.stages.stage01_tomap.impl.destructor.Destructor;
import kernel.tryapi.Try;

import java.util.Map;

public interface MapperDestructor<I> extends Destructor<I, String, Object> {


    static <I> MapperDestructor<I> of(Class<I> fromClazz) {
        return MapperDestructorImpl.of(fromClazz);
    }

    @Override
    Try<Map<String, Object>> destruct(I input);
}
