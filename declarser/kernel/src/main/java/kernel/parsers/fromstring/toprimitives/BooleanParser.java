package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class BooleanParser implements Function<String, Try<?>> {

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
        else return Try.go(() -> Boolean.parseBoolean(s))
                .enrichException(ex -> ParserException.of(s, Boolean.class, ex));
    }
}
