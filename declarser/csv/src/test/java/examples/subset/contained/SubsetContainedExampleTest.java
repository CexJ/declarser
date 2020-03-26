package examples.subset.contained;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import examples.subset.samples.SubsetExample;
import org.junit.jupiter.api.Test;

import static io.github.cexj.declarser.kernel.enums.SubsetType.CONTAINED;
import static org.junit.jupiter.api.Assertions.*;

public class SubsetContainedExampleTest {

    @Test
    public void parse_contained_input_return_success(){
        final var csv = "first";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withInputSubsetType(CONTAINED)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
        assertNull(value.getSecondString());
    }

    @Test
    public void parse_bijective_input_return_success(){
        final var csv = "first;second";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withInputSubsetType(CONTAINED)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getFirstString(),"first");
        assertEquals(value.getSecondString(),"second");
    }

    @Test
    public void parse_contains_input_return_failure(){
        final var csv = "first;second;third";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withInputSubsetType(CONTAINED)
                .build();
        final var tryDeclarser = declarserFactory.declarserOf(SubsetExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isFailure());
    }
}
