package examples;

import examples.samples.From;
import examples.samples.To;
import mapper.builder.MapperDeclarserBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapperExample {

    @Test
    public void test(){
        final var declarser = MapperDeclarserBuilder
                .from(From.class)
                .to(To.class)
                .build()
                .getValue();
        final var from = new From("first",2);
        final var result = declarser.parse(from).getValue();
        assertEquals(result.getClass(), To.class);
        assertNotNull(result.getFirstValue());
        assertEquals(result.getFirstValue(), from.getFirstValue());
        assertEquals(result.getSecondValue(), from.getSecondValue());
    }
}
