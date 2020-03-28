package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class CharacterParser implements Parser<String,Character> {

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
        if(s == null || s.isEmpty()) return Try.success(null);
        else return (s.length() == 1 ? Try.success(s.charAt(0))
                                    : Try.fail(ParserException.of(s, Character.class, new IllegalArgumentException())));

    }
}
