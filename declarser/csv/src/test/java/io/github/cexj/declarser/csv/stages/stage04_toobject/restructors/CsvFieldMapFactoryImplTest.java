package io.github.cexj.declarser.csv.stages.stage04_toobject.restructors;

import io.github.cexj.declarser.csv.stages.stage04_toobject.CsvFieldMapFactory;
import io.github.cexj.declarser.csv.stages.stage04_toobject.exceptions.RepeatedCsvColumnIndexException;
import io.github.cexj.declarser.csv.stages.stage04_toobject.restructors.sample.InvalidSample;
import io.github.cexj.declarser.csv.stages.stage04_toobject.restructors.sample.ValidSample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * A class is valid if it does not contains @CsvColumn annotations with repeated value
 */
public class CsvFieldMapFactoryImplTest {

    /*
     * GIVEN a valid sample class S
     * WHEN mapFieldNameColumn method is invoked with S
     * THEN the result is a success
     *  AND the values corresponds to field name -> column
     */
    @Test
    public void map_valid_class_return_success(){
        // GIVEN a valid sample class S
        // WHEN mapFieldNameColumn method is invoked with S
        final var result = CsvFieldMapFactory.getInstance().mapFieldNameColumn(ValidSample.class);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the values corresponds to field name -> column
        final var value = result.getValue();
        assertEquals(value.size(), 2);
        final var name = value.get("name");
        assertEquals(name, 0);
        final var age = value.get("age");
        assertEquals(age, 1);
    }

    /*
     * GIVEN a invalid sample class S
     * WHEN mapFieldNameColumn method is invoked with S
     * THEN the result is a failure
     *  AND the exception is of type RepeatedCsvColumnIndexException
     */
    @Test
    public void map_valid_class_return_failure(){
        // GIVEN a invalid sample class S
        // WHEN mapFieldNameColumn method is invoked with S
        final var result = CsvFieldMapFactory.getInstance().mapFieldNameColumn(InvalidSample.class);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of type RepeatedCsvColumnIndexException
        final var exception = result.getException();
        assertEquals(exception.getClass(), RepeatedCsvColumnIndexException.class);
    }
}
