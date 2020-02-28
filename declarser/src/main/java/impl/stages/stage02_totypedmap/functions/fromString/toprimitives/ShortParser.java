package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class ShortParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final ShortParser instance = new ShortParser();
    }

    public static ShortParser getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Try<Short> apply(String s) {
        return Try.go(() -> Short.parseShort(s));
    }
}
