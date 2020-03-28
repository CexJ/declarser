package examples.parallel;

import io.github.cexj.declarser.csv.CsvDeclarserFactory;
import examples.subset.samples.SubsetExample;
import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelExampleTest {

    @Test
    public void parse_contained_input_return_success(){
        final var csv = "first;second";
        final var declarserFactory = CsvDeclarserFactory.builder()
                .withParallelizationStrategy(ParallelizationStrategyEnum.PARALLEL)
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

}
