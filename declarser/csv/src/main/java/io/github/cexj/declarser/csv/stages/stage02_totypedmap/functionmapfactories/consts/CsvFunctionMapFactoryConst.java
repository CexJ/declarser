package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.consts;

import io.github.cexj.declarser.kernel.parsers.fromstring.todate.LocalDateParser;
import io.github.cexj.declarser.kernel.parsers.fromstring.todate.LocalDateTimeParser;
import io.github.cexj.declarser.kernel.parsers.fromstring.todate.ZonedDateTimeParser;
import io.github.cexj.declarser.kernel.parsers.fromstring.tonumber.BigDecimalParser;
import io.github.cexj.declarser.kernel.parsers.fromstring.tonumber.BigIntegerParser;
import io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive.*;
import io.github.cexj.declarser.kernel.parsers.fromstring.tostring.StringParser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CsvFunctionMapFactoryConst {

    private CsvFunctionMapFactoryConst(){}

    public static final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> sharedFunctionClassMap;
    static {
        sharedFunctionClassMap = new HashMap<>();
        sharedFunctionClassMap.put(LocalDateParser.class, arr -> LocalDateParser.getInstance(arr[0]));
        sharedFunctionClassMap.put(LocalDateTimeParser.class, arr -> LocalDateTimeParser.getInstance(arr[0]));
        sharedFunctionClassMap.put(ZonedDateTimeParser.class, arr -> ZonedDateTimeParser.getInstance(arr[0]));
        sharedFunctionClassMap.put(BigDecimalParser.class, arr -> BigDecimalParser.getInstance());
        sharedFunctionClassMap.put(BigIntegerParser.class, arr -> BigIntegerParser.getInstance());
        sharedFunctionClassMap.put(BooleanParser.class, arr -> BooleanParser.getInstance());
        sharedFunctionClassMap.put(CharacterParser.class, arr -> CharacterParser.getInstance());
        sharedFunctionClassMap.put(DoubleParser.class, arr -> DoubleParser.getInstance());
        sharedFunctionClassMap.put(FloatParser.class, arr -> FloatParser.getInstance());
        sharedFunctionClassMap.put(IntegerParser.class, arr -> IntegerParser.getInstance());
        sharedFunctionClassMap.put(LongParser.class, arr -> LongParser.getInstance());
        sharedFunctionClassMap.put(ShortParser.class, arr -> ShortParser.getInstance());
        sharedFunctionClassMap.put(StringParser.class, arr -> StringParser.getInstance());
    }

    public static final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap;

    public static final String LOCAL_DATE_DEFAULT_FORMAT = "yyyy'-'MM'-'dd";

    public static final String LOCAL_DATE_TIME_DEFAULT_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss";

    public static final String ZONED_DATE_TIME_DEFAULT_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss' 'z";

    static {
        autoFunctionClassMap = new HashMap<>();
        autoFunctionClassMap.put(LocalDate.class, LocalDateParser.getInstance(LOCAL_DATE_DEFAULT_FORMAT));
        autoFunctionClassMap.put(LocalDateTime.class, LocalDateTimeParser.getInstance(LOCAL_DATE_TIME_DEFAULT_FORMAT));
        autoFunctionClassMap.put(ZonedDateTime.class, ZonedDateTimeParser.getInstance(ZONED_DATE_TIME_DEFAULT_FORMAT));
        autoFunctionClassMap.put(BigDecimal.class, BigDecimalParser.getInstance());
        autoFunctionClassMap.put(BigInteger.class, BigIntegerParser.getInstance());
        autoFunctionClassMap.put(Boolean.class, BooleanParser.getInstance());
        autoFunctionClassMap.put(Character.class, CharacterParser.getInstance());
        autoFunctionClassMap.put(Double.class, DoubleParser.getInstance());
        autoFunctionClassMap.put(Float.class, FloatParser.getInstance());
        autoFunctionClassMap.put(Integer.class, IntegerParser.getInstance());
        autoFunctionClassMap.put(Long.class, LongParser.getInstance());
        autoFunctionClassMap.put(Short.class, ShortParser.getInstance());
        autoFunctionClassMap.put(String.class, StringParser.getInstance());
    }
}
