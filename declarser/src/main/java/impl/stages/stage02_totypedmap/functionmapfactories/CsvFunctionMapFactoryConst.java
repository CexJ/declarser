package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDate;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToZonedDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigDecimal;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigInteger;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.*;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.FromStringToString;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CsvFunctionMapFactoryConst {



    static final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> sharedFunctionClassMap = new HashMap<>();
    static {
        sharedFunctionClassMap.put(FromStringToLocalDate.class, arr -> FromStringToLocalDate.getInstance(arr[0]));
        sharedFunctionClassMap.put(FromStringToLocalDateTime.class, arr -> FromStringToLocalDateTime.getInstance(arr[0]));
        sharedFunctionClassMap.put(FromStringToZonedDateTime.class, arr -> FromStringToZonedDateTime.getInstance(arr[0]));
        sharedFunctionClassMap.put(FromStringToBigDecimal.class, arr -> FromStringToBigDecimal.getInstance());
        sharedFunctionClassMap.put(FromStringToBigInteger.class, arr -> FromStringToBigInteger.getInstance());
        sharedFunctionClassMap.put(FromStringToBoolean.class, arr -> FromStringToBoolean.getInstance());
        sharedFunctionClassMap.put(FromStringToCharacter.class, arr -> FromStringToCharacter.getInstance());
        sharedFunctionClassMap.put(FromStringToDouble.class, arr -> FromStringToDouble.getInstance());
        sharedFunctionClassMap.put(FromStringToFloat.class, arr -> FromStringToFloat.getInstance());
        sharedFunctionClassMap.put(FromStringToInteger.class, arr -> FromStringToInteger.getInstance());
        sharedFunctionClassMap.put(FromStringToLong.class, arr -> FromStringToLong.getInstance());
        sharedFunctionClassMap.put(FromStringToShort.class, arr -> FromStringToShort.getInstance());
        sharedFunctionClassMap.put(FromStringToString.class, arr -> FromStringToString.getInstance());
    }
}
