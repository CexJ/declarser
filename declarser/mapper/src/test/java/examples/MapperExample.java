package examples;

import examples.samples.From;
import examples.samples.To;
import kernel.tryapi.Try;
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
        final var result = declarser.apply(from).getValue();
        assertEquals(result.getClass(), To.class);
        assertNotNull(result.getFirstValue());
        assertEquals(result.getFirstValue(), from.getFirstValue());
        assertEquals(result.getSecondValue(), from.getSecondValue());
    }

    @Test
    public void test2(){
        final var declarser = MapperDeclarserBuilder
                .from(From.class)
                .to(To.class)
                .with("secondValue").getValue()
                .as(from -> Try.success(3))
                .build()
                .getValue();
        final var from = new From("first",2);
        final var result = declarser.apply(from).getValue();
        assertEquals(result.getClass(), To.class);
        assertNotNull(result.getFirstValue());
        assertEquals(result.getFirstValue(), from.getFirstValue());
        assertEquals(result.getSecondValue(), 3);
    }

}
