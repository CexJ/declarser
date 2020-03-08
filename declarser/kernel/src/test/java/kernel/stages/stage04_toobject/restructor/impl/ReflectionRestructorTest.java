package kernel.stages.stage04_toobject.restructor.impl;

import kernel.enums.SubsetType;
import kernel.stages.stage04_toobject.impl.restructor.impl.ReflectionRestructor;
import kernel.stages.stage04_toobject.restructor.impl.sample.TypeO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionRestructorTest {

    private static final class TypeK{}

    /*
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @Test
    public void restruct_a_class_from_valid_input_is_a_success(){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds, SubsetType.NONE, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND foreach Ki its value is Vi
        final var value = result.getValue();
        assertNotNull(value.getFirstField());
        assertEquals(value.getFirstField(), input.get(mapFileds.get("firstField")));
        assertNotNull(value.getSecondField());
        assertEquals(value.getSecondField(), input.get(mapFileds.get("secondField")));
    }

    /*
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki except for i in a set J
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach i in (1,...,N)\J Fi its value is Vi
     *  AND foreach j in J Fj is null
     */
    @Test
    public void restruct_a_class_missing_mapfield_entries_is_a_success(){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds, SubsetType.NONE, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND foreach Ki its value is Vi
        final var value = result.getValue();
        assertNotNull(value.getSecondField());
        assertEquals(value.getSecondField(), input.get(mapFileds.get("secondField")));
        // AND foreach j in J Fj is null
        assertNull(value.getFirstField());
    }

    /*
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @Test
    public void restruct_a_class_with_invalid_mapfield_entry_is_a_success(){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki plus some other entries Fj -> Kj
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        mapFileds.put("thirdField", thirdKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds, SubsetType.NONE, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND foreach Ki its value is Vi
        final var value = result.getValue();
        assertNotNull(value.getFirstField());
        assertEquals(value.getFirstField(), input.get(mapFileds.get("firstField")));
        assertNotNull(value.getSecondField());
        assertEquals(value.getSecondField(), input.get(mapFileds.get("secondField")));
    }

    /*
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      except for i in J<(1,...,N) for which there is no entry
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach i in (1,...,N)\J Fi its value is Vi
     *  AND foreach j in J Fj is null
     */
    @Test
    public void restruct_a_class_from_invalid_input_is_a_success(){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds, SubsetType.NONE, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     except for i in J<(1,...,N) for which there is no entry
        final var input = new HashMap<TypeK, Object>();
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND foreach Ki its value is Vi
        final var value = result.getValue();
        assertNotNull(value.getSecondField());
        assertEquals(value.getSecondField(), input.get(mapFileds.get("secondField")));
        // AND foreach j in J Fj is null
        assertNull(value.getFirstField());
    }
}
