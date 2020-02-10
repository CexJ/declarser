package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToInteger implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToInteger instance = new FromStringToInteger();
    }

    public static FromStringToInteger getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToInteger(){}

    @Override
    public Try<Integer> apply(String s) {
        return Try.go(() -> Integer.parseInt(s));
    }
}
