package examples.functiontype;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;
import examples.samples.from.From1;
import examples.samples.from.From2;
import examples.samples.from.FromWithOutsider;
import examples.samples.to.To1;
import examples.samples.to.To2;
import examples.samples.to.ToWithOutsider;
import mapper.builder.MapperDeclarserBuilder;
import org.junit.jupiter.api.Test;

import static kernel.enums.SubsetType.CONTAINS;
import static kernel.enums.SubsetType.NONE;
import static org.junit.jupiter.api.Assertions.*;

public class NoneExample {
    @Test
    public void mapping_in_bijection_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To2.class).withToFields(NONE).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), To2.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertNotNull(value.getSecondValue());
        assertEquals(value.getSecondValue(), from.getSecondValue());
    }

    @Test
    public void mapping_contains_class_return_success(){
        final var value1 = new FieldType1();
        final var from = new From1(value1);
        final var built = MapperDeclarserBuilder.from(From1.class).to(To2.class).withToFields(NONE).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), To2.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertNull(value.getSecondValue());
    }

    @Test
    public void mapping_contained_class_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To1.class).withToFields(NONE).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), To1.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
    }

    @Test
    public void mapping_with_outsiders_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var from = new FromWithOutsider(value1,value2);
        final var built = MapperDeclarserBuilder.from(FromWithOutsider.class).to(ToWithOutsider.class).withToFields(NONE).build();
        assertTrue(built.isSuccess());
        final var declarser = built.getValue();
        final var result = declarser.apply(from);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getClass(), ToWithOutsider.class);
        assertNotNull(value.getFirstValue());
        assertEquals(value.getFirstValue(), from.getFirstValue());
        assertNull(value.getToOutsider());
    }
}
