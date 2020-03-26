package examples.functiontype;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;
import examples.samples.from.From1;
import examples.samples.from.From2;
import examples.samples.from.FromWithOutsider;
import examples.samples.to.To1;
import examples.samples.to.To2;
import examples.samples.to.ToWithOutsider;
import io.github.cexj.declarser.mapper.builder.MapperDeclarserBuilder;
import org.junit.jupiter.api.Test;

import static io.github.cexj.declarser.kernel.enums.SubsetType.BIJECTIVE;
import static org.junit.jupiter.api.Assertions.*;

public class BijectiveExample {
    @Test
    public void mapping_in_bijection_return_success(){
        final var value1 = new FieldType1();
        final var value2 = new FieldType2();
        final var from = new From2(value1,value2);
        final var built = MapperDeclarserBuilder.from(From2.class).to(To2.class).withTargetFields(BIJECTIVE).build();
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
    public void mapping_contains_class_return_failure(){
        final var built = MapperDeclarserBuilder.from(From1.class).to(To2.class).withTargetFields(BIJECTIVE).build();
        assertTrue(built.isFailure());
    }

    @Test
    public void mapping_contained_class_return_failure(){
        final var built = MapperDeclarserBuilder.from(From2.class).to(To1.class).withTargetFields(BIJECTIVE).build();
        assertTrue(built.isFailure());
    }

    @Test
    public void mapping_with_outsiders_return_failure(){
        final var built = MapperDeclarserBuilder.from(FromWithOutsider.class).to(ToWithOutsider.class).withTargetFields(BIJECTIVE).build();
        assertTrue(built.isFailure());
    }
}
