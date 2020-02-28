package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class BooleanParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final BooleanParser instance = new BooleanParser();
    }

    public static BooleanParser getInstance() {
        return InstanceHolder.instance;
    }

    private BooleanParser(){}

    @Override
    public Try<Boolean> apply(String s) {
        return Try.go(() -> Boolean.parseBoolean(s));
    }
}
