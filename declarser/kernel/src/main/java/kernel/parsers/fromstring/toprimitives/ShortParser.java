package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

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
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.go(() -> Short.parseShort(s))
                .enrichException(ex -> ParserException.of(s, Short.class, ex));
    }
}
