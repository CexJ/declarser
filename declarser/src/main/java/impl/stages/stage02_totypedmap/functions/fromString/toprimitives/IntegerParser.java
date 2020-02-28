package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class IntegerParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final IntegerParser instance = new IntegerParser();
    }

    public static IntegerParser getInstance() {
        return InstanceHolder.instance;
    }

    private IntegerParser(){}

    @Override
    public Try<Integer> apply(String s) {
        return Try.go(() -> Integer.parseInt(s));
    }
}
