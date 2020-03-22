package mapper.stages.stage_01.destructor;

import mapper.stages.stage_01.destructor.sample.TypeI;
import mapper.stages.stage_01.destructor.sample.TypeK1;
import mapper.stages.stage_01.destructor.sample.TypeK2;
import org.junit.jupiter.api.Test;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapperDestructorImplTest {


    /*
     * GIVEN a type I with fields with names N1, ..., Nn
     *  AND a map M with elements (S1, F1), ..., (Sm, Fm) where
     *      Sj is a string Fj is a function from I to Try<?> for each j in 1, ..., m
     *      and Sj is not equal to Ni for each j in 1, ..., m and i in 1, ..., n
     *  AND a MapperDestructor constructed with I and M
     *  AND a value of type I V(v1,..., vn) where vi is the value of the field with name Ni
     * WHEN the method destruct is invoked with V
     * THEN the result is a success
     *  AND the value is a map MV such that
     *      the keys is the union N1, ..., Nn and S1, ..., Sm
     *  AND MV.get(Ni) = vi for each i in 1, ..., n
     *  AND MV.get(Sj) = Fj(V) for each j in 1, ..., m
     */
    @Test
    public void destructing_without_intersection_return_success(){
        // GIVEN a type I with fields with names N1, ..., Nn
        // AND a map M with elements (S1, F1), ..., (Sm, Fm) where
        //     Sj is a string Fj is a function from I to Try<?> for each j in 1, ..., m
        //     and Sj is not equal to Ni for each j in 1, ..., m and i in 1, ..., n
        final var fieldFunctionMap = new HashMap<String, Function<TypeI, Try<?>>>();
        final var value2 = new TypeK2();
        final Function<TypeI, Try<?>> function = i -> Try.success(value2);
        fieldFunctionMap.put("value2", function);
        // AND a MapperDestructor constructed with I and M
        final var mapperDestructor = MapperDestructor.of(TypeI.class, fieldFunctionMap);
        // AND a value of type I V(v1,..., vn) where vi is the value of the field with name Ni
        final var value1 = new TypeK1();
        final var input = new TypeI(value1);
        // WHEN the method destruct is invoked with V
        final var result = mapperDestructor.destruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a map MV such that
        //     the keys is the union N1, ..., Nn and S1, ..., Sm
        final var value = result.getValue();
        assertEquals(value.size(), input.getClass().getDeclaredFields().length + fieldFunctionMap.size());
        // AND MV.get(Ni) = vi for each i in 1, ..., n
        assertTrue(value.get("value1").isSuccess());
        assertEquals(value.get("value1").getValue(), value1);
        // AND MV.get(Sj) = Fj(V) for each j in 1, ..., m
        assertTrue(value.get("value2").isSuccess());
        assertTrue(function.apply(input).isSuccess());
        assertEquals(value.get("value2").getValue(), function.apply(input).getValue());
    }

    /*
     * GIVEN a type I with fields with names N1, ..., Nn
     *  AND a map M with elements (S1, F1), ..., (Sm, Fm) where
     *      Sj is a string Fj is a function from I to Try<?> for each j in 1, ..., m
     *      and Si is not equal to Nj for some j in 1, ..., m and i in 1, ..., n
     *  AND a MapperDestructor constructed with I and M
     *  AND a value of type I V(v1,..., vn) where vi is the value of the field with name Ni
     * WHEN the method destruct is invoked with V
     * THEN the result is a success
     *  AND the value is a map MV such that
     *      the keys is the union N1, ..., Nn and S1, ..., Sm
     *  AND MV.get(Ni) = vi for each i in 1, ..., n except for i in the intersection
     *  AND MV.get(Sj) = Fj(V) for each j in 1, ..., m
     */
    @Test
    public void destructing_with_intersection_return_success_with_custom_as_priority(){
        // GIVEN a type I with fields with names N1, ..., Nn
        // AND a map M with elements (S1, F1), ..., (Sm, Fm) where
        //     Sj is a string Fj is a function from I to Try<?> for each j in 1, ..., m
        //     and Sj is not equal to Ni for each j in 1, ..., m and i in 1, ..., n
        final var fieldFunctionMap = new HashMap<String, Function<TypeI, Try<?>>>();
        final var value2 = new TypeK1();
        final Function<TypeI, Try<?>> function = i -> Try.success(value2);
        fieldFunctionMap.put("value1", function);
        // AND a MapperDestructor constructed with I and M
        final var mapperDestructor = MapperDestructor.of(TypeI.class, fieldFunctionMap);
        // AND a value of type I V(v1,..., vn) where vi is the value of the field with name Ni
        final var value1 = new TypeK1();
        final var input = new TypeI(value1);
        // WHEN the method destruct is invoked with V
        final var result = mapperDestructor.destruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a map MV such that
        //     the keys is the union N1, ..., Nn and S1, ..., Sm
        final var value = result.getValue();
        assertEquals(value.size(), Stream.of(
                Stream.of(input.getClass().getDeclaredFields()).map(Field::getName),
                fieldFunctionMap.keySet().stream()).flatMap(i -> i)
                .collect(Collectors.toSet()).size());
        // AND MV.get(Ni) = vi for each i in 1, ..., n except for i in the intersection
        // AND MV.get(Sj) = Fj(V) for each j in 1, ..., m
        assertTrue(value.get("value1").isSuccess());
        assertTrue(function.apply(input).isSuccess());
        assertEquals(value.get("value1").getValue(), function.apply(input).getValue());
    }
}
