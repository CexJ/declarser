package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public final class ShortParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final ShortParser instance = new ShortParser();
    }

    public static ShortParser getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Try<Short> apply(
            final String s) {
        return Try.go(() -> Short.parseShort(s));
    }
}
