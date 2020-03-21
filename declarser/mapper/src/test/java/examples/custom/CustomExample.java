package examples.custom;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;
import examples.samples.fields.FieldType3;
import examples.samples.from.From1;
import examples.samples.from.From2;
import examples.samples.from.FromWithOutsider;
import examples.samples.to.To1;
import examples.samples.to.To2;
import examples.samples.to.To3;
import examples.samples.to.ToWithOutsider;
import kernel.tryapi.Try;
import mapper.builder.MapperDeclarserBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomExample {

    @Test
    public void mapping_custom_missing_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var value3 = new FieldType3();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To3.class)
                .with("thirdValue")
                .as(from2 -> Try.success(value3)).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), To3.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertNotNull(value.getSecondValue());
        assertEquals(value.getSecondValue(), from.getSecondValue());
        assertNotNull(value.getThirdValue());
        assertEquals(value.getThirdValue(), value3);
    }

    @Test
    public void mapping_custom_field_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var newValue2 = new FieldType2();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To2.class)
                .with("secondValue")
                .as(from2 -> Try.success(newValue2)).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), To2.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertNotNull(value.getSecondValue());
        assertEquals(value.getSecondValue(), newValue2);
    }

    @Test
    public void mapping_custom_inexisting_field_return_failure(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var newValue2 = new FieldType2();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To2.class)
                .with("NOT A FIELD")
                .as(from2 -> Try.success(newValue2)).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isFailure());
    }
}
