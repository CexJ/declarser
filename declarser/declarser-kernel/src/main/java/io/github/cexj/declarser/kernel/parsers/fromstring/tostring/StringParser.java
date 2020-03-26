package io.github.cexj.declarser.kernel.parsers.fromstring.tostring;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;


public final class StringParser implements Parser<String> {

    private static class InstanceHolder {
        private static final StringParser instance = new StringParser();
    }

    public static StringParser getInstance() {
        return InstanceHolder.instance;
    }
    
    private StringParser(){}

    @Override
    public Try<String> apply(
            final String s) {
        return Try.success(s);
    }
}
