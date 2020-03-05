package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class CharacterParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final CharacterParser instance = new CharacterParser();
    }

    public static CharacterParser getInstance() {
        return InstanceHolder.instance;
    }

    private CharacterParser(){}

    @Override
    public Try<Character> apply(
            final String s) {
        if(s == null || s.isEmpty()) return null;
        else return (s.length() == 1 ? Try.success(s.charAt(0))
                                    : Try.fail(ParseException.of(s, Character.class, new IllegalArgumentException())));

    }
}
