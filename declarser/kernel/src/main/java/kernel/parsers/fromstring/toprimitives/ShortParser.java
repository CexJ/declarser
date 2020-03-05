package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.Parser;
import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class ShortParser implements Parser<String, Short> {

    private static class InstanceHolder {
        private static final ShortParser instance = new ShortParser();
    }

    public static ShortParser getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Try<Short> apply(
            final String s) {
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> Short.parseShort(s))
                .enrichException(ex -> ParseException.of(s, Short.class, ex));
    }
}
