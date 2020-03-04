package csv.stages.stage02_totypedmap.functions.fromString.toprimitives;

import kernel.tryapi.Try;

import java.util.function.Function;

public final class LongParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final LongParser instance = new LongParser();
    }

    public static LongParser getInstance() {
        return InstanceHolder.instance;
    }

    private LongParser(){}


    @Override
    public Try<Long> apply(
            final String s) {
        return Try.go(() -> Long.parseLong(s));
    }
}
