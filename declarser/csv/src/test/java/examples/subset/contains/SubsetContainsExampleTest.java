package examples.subset.contains;

import csv.CsvDeclarserFactory;
import examples.prevalidation.local.samples.LocalPrevalidationExample;
import examples.subset.samples.SubsetExample;
import kernel.enums.SubsetType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubsetContainsExampleTest {

    @Test
    public void parse_valid_input_return_success(){
        final var csv = "first";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withAnnotationsSubsetType(SubsetType.CONTAINS)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.parse(csv);
        result.getException().printStackTrace();
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
    }

    @Test
    public void parse_invalid_input_return_failure(){
        final var csv = "first;second;third";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withAnnotationsSubsetType(SubsetType.CONTAINS)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.parse(csv);
        assertTrue(result.isFailure());
    }
}
