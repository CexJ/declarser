package impl.stages.stage02_totypedmap.functionmapfactories;

import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDate;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToLocalDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.todate.FromStringToZonedDateTime;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigDecimal;
import impl.stages.stage02_totypedmap.functions.fromString.tonumber.FromStringToBigInteger;
import impl.stages.stage02_totypedmap.functions.fromString.toprimitives.*;
import impl.stages.stage02_totypedmap.functions.fromString.tostring.FromStringToString;
import kernel.Declarser;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CsvFunctionMapFactory {

    private final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap = new HashMap<>();
    {
        functionClassMap.put(FromStringToLocalDate.class, arr -> FromStringToLocalDate.getInstance(arr[0]));
        functionClassMap.put(FromStringToLocalDateTime.class, arr -> FromStringToLocalDateTime.getInstance(arr[0]));
        functionClassMap.put(FromStringToZonedDateTime.class, arr -> FromStringToZonedDateTime.getInstance(arr[0]));
        functionClassMap.put(FromStringToBigDecimal.class, arr -> FromStringToBigDecimal.getInstance());
        functionClassMap.put(FromStringToBigInteger.class, arr -> FromStringToBigInteger.getInstance());
        functionClassMap.put(FromStringToBoolean.class, arr -> FromStringToBoolean.getInstance());
        functionClassMap.put(FromStringToCharacter.class, arr -> FromStringToCharacter.getInstance());
        functionClassMap.put(FromStringToDouble.class, arr -> FromStringToDouble.getInstance());
        functionClassMap.put(FromStringToFloat.class, arr -> FromStringToFloat.getInstance());
        functionClassMap.put(FromStringToInteger.class, arr -> FromStringToInteger.getInstance());
        functionClassMap.put(FromStringToLong.class, arr -> FromStringToLong.getInstance());
        functionClassMap.put(FromStringToShort.class, arr -> FromStringToShort.getInstance());
        functionClassMap.put(FromStringToString.class, arr -> FromStringToString.getInstance());
    }


    private CsvFunctionMapFactory(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customMap) {
        customMap.entrySet().forEach(kv -> functionClassMap.put(kv.getKey(),kv.getValue()));
    }

    public static CsvFunctionMapFactory of(Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customMap){
        return new CsvFunctionMapFactory(customMap);
    }

    public Map<Integer, Function<String, Try<?>>> getMap(Class<?> clazz){
        return null;
    }
}
