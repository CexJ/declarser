package kernel.stages.stage04_toobject.restructor.impl;

import kernel.enums.SubsetType;
import kernel.exceptions.SubsetTypeException;
import kernel.stages.stage04_toobject.impl.restructor.impl.ReflectionRestructor;
import kernel.stages.stage04_toobject.restructor.impl.sample.TypeO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionRestructorTest {

    private static final class TypeK{}

    /*
     * FOR Input SubsetType CONTAINED, CONTAINS, BIJECTIVE, NONE
     * AND Field SubsetType CONTAINED, CONTAINS, BIJECTIVE, NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @CsvSource({
            "CONTAINED, CONTAINED",
            "CONTAINED, CONTAINS",
            "CONTAINED, BIJECTIVE",
            "CONTAINED, NONE",
            "CONTAINS,  CONTAINED",
            "CONTAINS,  CONTAINS",
            "CONTAINS,  BIJECTIVE",
            "CONTAINS,  NONE",
            "BIJECTIVE, CONTAINED",
            "BIJECTIVE, CONTAINS",
            "BIJECTIVE, BIJECTIVE",
            "BIJECTIVE, NONE",
            "NONE,      CONTAINED",
            "NONE,      CONTAINS",
            "NONE,      BIJECTIVE",
            "NONE,      NONE",
    })
    public void restruct_a_class_from_valid_input_is_a_success(String inputSubsetType, String fieldSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                SubsetType.valueOf(inputSubsetType), SubsetType.valueOf(fieldSubsetType)).getValue();
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
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType NONE
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_missing_mapfield_entries_is_a_success(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki except for i in a set J
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_missing_mapfield_entries_is_a_failure(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType CONTAINED
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_missing_mapfield_entries_is_a_success_fields_contained(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType CONTAINED
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki except for i in a set J
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_missing_mapfield_entries_is_a_failure_fields_contained(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);

    }

    /*
     * FOR Input SubsetType CONTAINS, CONTAINED, BIJECTIVE, NONE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki except for i in a set J
     * WHEN a restructor R is constructed with C and M
     * THEN is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class)
    public void restruct_a_class_missing_mapfield_entries_is_a_failure_fields_contains(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // WHEN a restructor R is constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED);
        // THEN is a failure
        assertTrue(restructor.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = restructor.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);

    }

    /*
     * FOR Input SubsetType CONTAINS, CONTAINED, BIJECTIVE, NONE
     * AND Field SubsetType BIJECTIVE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki except for i in a set J
     * WHEN a restructor R is constructed with C and M
     * THEN is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class)
    public void restruct_a_class_missing_mapfield_entries_is_a_failure_fields_bijective(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        // LET M be the map Fi -> Ki except for some i in a set J
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("secondField", secondKey);
        // WHEN a restructor R is constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE);
        // THEN is a failure
        assertTrue(restructor.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = restructor.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }


    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_success(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
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
     * FOR Input SubsetType CONTAINS, BIJECTIVE
     * AND Field SubsetType NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "BIJECTIVE"})
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_failure(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
    }


    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_success_fields_contains(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
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
     * FOR Input SubsetType CONTAINS, BIJECTIVE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "BIJECTIVE"})
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_failure_fields_contains(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, CONTAINED, BIJECTIVE, NONE
     * AND Field SubsetType CONTAINED
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class)
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_success_fields_contained(SubsetType inputSubsetType){
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
        // WHEN a restructor R is constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS);
        // THEN is a failure
        assertTrue(restructor.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = restructor.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, CONTAINED, BIJECTIVE, NONE
     * AND Field SubsetType BIJECTIVE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki plus some other entries Fj -> Kj
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Fi its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class)
    public void restrict_a_class_with_excessive_mansfield_entry_is_a_success_fields_bijective(SubsetType inputSubsetType){
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
        // WHEN a restructor R is constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE);
        // THEN is a failure
        assertTrue(restructor.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = restructor.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }


    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType NONE
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restruct_a_class_from_missing_input_is_a_success(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
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

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType CONTAINS
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restruct_a_class_from_missing_input_is_a_success_fields_contains(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
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

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      except for i in J<(1,...,N) for which there is no entry
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "BIJECTIVE"})
    public void restruct_a_class_from_missing_input_is_a_failure_fields_contains(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     except for i in J<(1,...,N) for which there is no entry
        final var input = new HashMap<TypeK, Object>();
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType CONTAINED
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restruct_a_class_from_missing_input_is_a_success_fields_contained(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
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

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType CONTAINED
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      except for i in J<(1,...,N) for which there is no entry
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "BIJECTIVE"})
    public void restruct_a_class_from_missing_input_is_a_failure_fields_contained(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     except for i in J<(1,...,N) for which there is no entry
        final var input = new HashMap<TypeK, Object>();
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType BIJECTIVE
     *
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
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "NONE"})
    public void restruct_a_class_from_missing_input_is_a_success_fields_bijective(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE).getValue();
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

    /*
     * FOR Input SubsetType CONTAINED, NONE
     * AND Field SubsetType BIJECTIVE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      except for i in J<(1,...,N) for which there is no entry
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "BIJECTIVE"})
    public void restruct_a_class_from_missing_input_is_a_failure_fields_bijective(SubsetType inputSubsetType){
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
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     except for i in J<(1,...,N) for which there is no entry
        final var input = new HashMap<TypeK, Object>();
        input.put(secondKey, 2);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a success
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Ki its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_from_excessive_input_is_a_success(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType NONE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_from_excessive_input_is_a_failure(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.NONE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Ki its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_from_excessive_input_is_a_success_fields_contains(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType CONTAINS
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_from_excessive_input_is_a_failure_fields_contains(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINED).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType CONTAINED
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Ki its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_from_excessive_input_is_a_success_fields_contained(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType CONTAINED
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_from_excessive_input_is_a_failure_fields_contained(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.CONTAINS).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }

    /*
     * FOR Input SubsetType CONTAINS, NONE
     * AND Field SubsetType BIJECTIVE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a success
     *  AND foreach Ki its value is Vi
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINED", "NONE"})
    public void restruct_a_class_from_excessive_input_is_a_success_fields_bijective(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
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
     * FOR Input SubsetType CONTAINED, BIJECTIVE
     * AND Field SubsetType BIJECTIVE
     *
     * GIVEN a class C with a list of fields with names: F1, F2, ..., FN
     *  AND a list of key of type TypeK: K1,K2, ..., KN
     *  LET M be the map Fi -> Ki
     *  AND a restructor R constructed with C and M
     *  AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
     *      plus some other entries Kj -> Vj
     * WHEN the restruct method is invoked with I
     * THEN the result is a failure
     *  AND the Exception is of type SubsetException
     */
    @ParameterizedTest
    @EnumSource(value = SubsetType.class, names = {"CONTAINS", "BIJECTIVE"})
    public void restruct_a_class_from_excessive_input_is_a_failure_fields_bijective(SubsetType inputSubsetType){
        // GIVEN a class C with a list of fields with names: F1, F2, ..., FN
        final var clazz = TypeO.class;
        // AND a list of key of type TypeK: K1,K2, ..., KN
        final var firstKey = new TypeK();
        final var secondKey = new TypeK();
        final var thirdKey = new TypeK();
        // LET M be the map Fi -> Ki
        final var mapFileds = new HashMap<String, TypeK>();
        mapFileds.put("firstField",firstKey);
        mapFileds.put("secondField", secondKey);
        // AND a restructor R constructed with C and M
        final var restructor = ReflectionRestructor.of(clazz, mapFileds,
                inputSubsetType, SubsetType.BIJECTIVE).getValue();
        // AND a map I that map Ki -> Vi, where Vi are values of the type of Ki
        //     plus some other entries Kj -> Vj
        final var input = new HashMap<TypeK, Object>();
        input.put(firstKey, "firstValue");
        input.put(secondKey, 2);
        input.put(thirdKey, true);
        // WHEN the restruct method is invoked with I
        final var result = restructor.restruct(input);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the Exception is of type SubsetException
        final var exception = result.getException();
        assertEquals(exception.getClass(), SubsetTypeException.class);
    }
}
