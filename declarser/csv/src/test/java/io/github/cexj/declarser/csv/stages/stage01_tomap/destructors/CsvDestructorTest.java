package io.github.cexj.declarser.csv.stages.stage01_tomap.destructors;

import io.github.cexj.declarser.csv.stages.stage01_tomap.exceptions.CsvNullInputException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CsvDestructorTest {

    /*
     * GIVEN an input string I
     *  AND a separator S
     *  AND a destructor constructed with S
     * WHEN the method destruct is invoked with I
     * THEN the result is a success
     *  AND the value is the map from integers to I.split() starting from 0
     */
    @Test
    public void destruct_a_valid_input_is_a_success(){
        // GIVEN an input string I
        final var input = "THIS;IS;A;TEST";
        // AND a separator S
        final var separator = ";";
        // AND a destructor constructed with S
        final var destructor = CsvDestructor.of(separator);
        // WHEN the method destruct is invoked with I
        final var result = destructor.destruct(input);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is the map from integers to I.split() starting from 0
        final var value = result.getValue();
        final var map = new HashMap<Integer, String>();
        map.put(0,"THIS");
        map.put(1,"IS");
        map.put(2,"A");
        map.put(3,"TEST");
        assertEquals(value, map);
    }

    /*
     * GIVEN a destructor constructed
     * WHEN the method destruct is invoked with null
     * THEN the result is a failure
     *  AND the exception is of the type CsvNullInputException
     */
    @Test
    public void destruct_a_null_input_is_a_failure(){
        // GIVEN a destructor constructed
        final var destructor = CsvDestructor.of(";");
        // WHEN the method destruct is invoked with null
        final var result = destructor.destruct(null);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type CsvNullInputException
        final var exception = result.getException();
        assertEquals(exception.getClass(), CsvNullInputException.class);
    }
}
