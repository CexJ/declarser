package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public final class DoubleParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final DoubleParser instance = new DoubleParser();
    }

    public static DoubleParser getInstance() {
        return InstanceHolder.instance;
    }

    private DoubleParser(){}


    @Override
    public Try<Double> apply(final String s) {
        return Try.go(() -> Double.parseDouble(s));
    }
}
