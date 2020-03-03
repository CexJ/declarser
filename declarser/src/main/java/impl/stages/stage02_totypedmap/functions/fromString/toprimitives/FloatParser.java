package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public final class FloatParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FloatParser instance = new FloatParser();
    }

    public static FloatParser getInstance() {
        return InstanceHolder.instance;
    }

    private FloatParser(){}

    @Override
    public Try<Float> apply(
            final String s) {
        return Try.go(() -> Float.parseFloat(s));
    }
}
