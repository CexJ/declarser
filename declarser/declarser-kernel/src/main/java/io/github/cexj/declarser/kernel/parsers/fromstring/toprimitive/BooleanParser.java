package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class BooleanParser implements Parser<String,Boolean> {

    private static class InstanceHolder {
        private static final BooleanParser instance = new BooleanParser();
    }

    public static BooleanParser getInstance() {
        return InstanceHolder.instance;
    }

    private BooleanParser(){}

    @Override
    public Try<Boolean> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.success(Boolean.parseBoolean(s));
    }
}
