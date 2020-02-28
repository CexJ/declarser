package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.stage02_totypedmap.functions.fromString.todate.LocalDateParser;
import impl.stages.stage02_totypedmap.functions.fromString.todate.LocalDateTimeParser;
import impl.stages.stage02_totypedmap.functions.fromString.todate.ZonedDateTimeParser;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.BigDecimalParser;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.BigIntegerParser;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.*;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.StringParser;
import utils.tryapi.Try;

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
}
