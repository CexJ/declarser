package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import javax.naming.directory.InvalidAttributesException;
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
        return s.length() == 1 ? Try.success(s.charAt(0))
                               : Try.fail(new InvalidAttributesException("Expecting a char but found: "+s));
    }
}
