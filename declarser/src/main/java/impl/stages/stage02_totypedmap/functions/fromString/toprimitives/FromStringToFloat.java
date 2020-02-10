package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToFloat implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToFloat instance = new FromStringToFloat();
    }

    public static FromStringToFloat getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToFloat(){}

    @Override
    public Try<Float> apply(String s) {
        return Try.go(() -> Float.parseFloat(s));
    }
}
