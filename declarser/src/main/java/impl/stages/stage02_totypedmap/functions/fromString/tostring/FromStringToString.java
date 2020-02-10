package impl.stages.stage02_totypedmap.functions.fromString.tostring;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToString implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToString instance = new FromStringToString();
    }

    public static FromStringToString getInstance() {
        return InstanceHolder.instance;
    }
    
    private FromStringToString(){}

    @Override
    public Try<String> apply(String s) {
        return Try.success(s);
    }
}
