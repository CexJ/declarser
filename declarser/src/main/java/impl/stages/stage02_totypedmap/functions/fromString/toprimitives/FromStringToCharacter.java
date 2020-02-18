package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import javax.naming.directory.InvalidAttributesException;
import java.util.function.Function;

public class FromStringToCharacter implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToCharacter instance = new FromStringToCharacter();
    }

    public static FromStringToCharacter getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToCharacter(){}

    @Override
    public Try<Character> apply(String s) {
        return s.length() == 1 ? Try.success(Character.valueOf(s.charAt(0)))
                               : Try.fail(new InvalidAttributesException("Expecting a char but found: "+s));
    }
}
