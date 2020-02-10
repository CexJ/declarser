package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToLong implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToLong instance = new FromStringToLong();
    }

    public static FromStringToLong getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToLong(){}


    @Override
    public Try<Long> apply(String s) {
        return Try.go(() -> Long.parseLong(s));
    }
}
