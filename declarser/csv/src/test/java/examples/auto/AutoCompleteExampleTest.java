package examples.auto;

import csv.CsvDeclarserFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AutoCompleteExampleTest {


    @Test
    public void all_possible_auto_parse(){
        final var csv =
                "2020-03-05;"+
                "2020-03-05T23:59:59;"+
                "2020-03-05T23:59:59 UTC;"+
                "1.1;"+
                "2;"+
                "true;"+
                "a;"+
                "3.3;"+
                "4.4;"+
                "5;"+
                "6;"+
                "7;"+
                "string";

        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(examples.auto.samples.AutoCompleteExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.apply(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getaLocalDate(), LocalDate.of(2020,3,5) );
        assertEquals(value.getaLocalDateTime(), LocalDateTime.of(2020,3,5,23,59, 59) );
        assertEquals(value.getaZonedDateTime(), ZonedDateTime.of(LocalDateTime.of(2020,3,5,23,59,59), ZoneId.of("UTC")));
        assertEquals(value.getaBigDecimal(), BigDecimal.valueOf(1.1));
        assertEquals(value.getaBigInteger(), BigInteger.valueOf(2));
        assertEquals(value.getaBoolean(), Boolean.TRUE);
        assertEquals(value.getaCharacter(), 'a');
        assertEquals(value.getaDouble(), 3.3);
        assertEquals(value.getaFloat(), 4.4f);
        assertEquals(value.getAnInteger(), 5);
        assertEquals(value.getaLong(), 6L);
        final short aShort = 7;
        assertEquals(value.getaShort(), aShort);
        assertEquals(value.getaString(), "string");

    }
}
