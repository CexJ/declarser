package impl.stages.stage02_totypedmap.functions.fromString.tostring;

import utils.tryapi.Try;

import java.util.function.Function;

public final class StringParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final StringParser instance = new StringParser();
    }

    public static StringParser getInstance() {
        return InstanceHolder.instance;
    }
    
    private StringParser(){}

    @Override
    public Try<String> apply(final String s) {
        return Try.success(s);
    }
}
