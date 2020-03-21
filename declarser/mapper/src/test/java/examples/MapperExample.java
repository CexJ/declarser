package examples;

import examples.samples.From;
import examples.samples.From2;
import examples.samples.To;
import examples.samples.To2;
import kernel.tryapi.Try;
import mapper.builder.MapperDeclarserBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
                .with("secondValue")
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

    @Test
    public void test3(){
        final var declarser = MapperDeclarserBuilder
                .from(From.class)
                .to(To2.class)
                .with("thirdValue")
                .as(from -> Try.success(3))
                .build()
                .getValue();
        final var from = new From("first",2);
        final var result = declarser.apply(from);
        final var value = result.getValue();
        assertEquals(value.getClass(), To2.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertEquals(value.getSecondValue(), from.getSecondValue());
        assertEquals(value.getThirdValue(), 3);
    }

    @Test
    public void test4(){
        final var declarser = MapperDeclarserBuilder
                .from(From.class)
                .to(To2.class)
                .with("NOT A FIELD")
                .as(from -> Try.success(3))
                .build()
                .getValue();
        final var from = new From("first",2);
        final var result = declarser.apply(from);
        assertTrue(result.isFailure());
    }

    @Test
    public void test5(){
        final var declarser = MapperDeclarserBuilder
                .from(From.class)
                .to(To2.class)
                .build()
                .getValue();
        final var from = new From("first",2);
        final var result = declarser.apply(from);
        final var value = result.getValue();
        assertEquals(value.getClass(), To2.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertEquals(value.getSecondValue(), from.getSecondValue());
        assertNull(value.getThirdValue());
    }

    @Test
    public void test6(){
        final var declarser = MapperDeclarserBuilder
                .from(From2.class)
                .to(To.class)
                .build()
                .getValue();
        final var from = new From2("first",2,3);
        final var result = declarser.apply(from).getValue();
        assertEquals(result.getClass(), To.class);
        assertNotNull(result.getFirstValue());
        assertEquals(result.getFirstValue(), from.getFirstValue());
        assertEquals(result.getSecondValue(), from.getSecondValue());
    }


}
