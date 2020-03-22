package examples.auto.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class AutoCompleteExample {

    @CsvColumn(0)
    private LocalDate aLocalDate;

    @CsvColumn(1)
    private LocalDateTime aLocalDateTime;

    @CsvColumn(2)
    private ZonedDateTime aZonedDateTime;

    @CsvColumn(3)
    private BigDecimal aBigDecimal;

    @CsvColumn(4)
    private BigInteger aBigInteger;

    @CsvColumn(5)
    private Boolean aBoolean;

    @CsvColumn(6)
    private Character aCharacter;

    @CsvColumn(7)
    private Double aDouble;

    @CsvColumn(8)
    private Float aFloat;

    @CsvColumn(9)
    private Integer anInteger;

    @CsvColumn(10)
    private Long aLong;

    @CsvColumn(11)
    private Short aShort;

    @CsvColumn(12)
    private String aString;

    public LocalDate getaLocalDate() {
        return aLocalDate;
    }

    public LocalDateTime getaLocalDateTime() {
        return aLocalDateTime;
    }

    public ZonedDateTime getaZonedDateTime() {
        return aZonedDateTime;
    }

    public BigDecimal getaBigDecimal() {
        return aBigDecimal;
    }

    public BigInteger getaBigInteger() {
        return aBigInteger;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public Character getaCharacter() {
        return aCharacter;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public Integer getAnInteger() {
        return anInteger;
    }

    public Long getaLong() {
        return aLong;
    }

    public Short getaShort() {
        return aShort;
    }

    public String getaString() {
        return aString;
    }
}
