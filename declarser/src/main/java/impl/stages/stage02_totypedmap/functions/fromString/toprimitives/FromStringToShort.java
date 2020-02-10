package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToShort implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToShort instance = new FromStringToShort();
    }

    public static FromStringToShort getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Try<Short> apply(String s) {
        return Try.go(() -> Short.parseShort(s));
    }
}
