package io.github.cexj.declarser.kernel.validations.impl.fromstring;

import io.github.cexj.declarser.kernel.validations.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public final class StringPatternValidator implements Validator<String> {


    private final Pattern pattern;

    private static final Map<String, StringPatternValidator> instancesMap = new HashMap<>();

    public static synchronized StringPatternValidator getInstance(
            final String format) {
        if(instancesMap.get(format) == null){
            StringPatternValidator newFromStringToLocalDate = new StringPatternValidator(format);
            instancesMap.put(format, newFromStringToLocalDate);
        }
        return instancesMap.get(format);
    }

    private StringPatternValidator(
            final String regex){
        this.pattern = Pattern.compile(regex, Pattern.MULTILINE);
    }

    @Override
    public Optional<? extends Exception> apply(
            final String s) {
        return s == null                     ? Optional.of(NonMathingPatternStringException.of(pattern.pattern(), s)) :
               !pattern.matcher(s).matches() ? Optional.of(NonMathingPatternStringException.of(pattern.pattern(), s)) :
                                               Optional.empty();
    }

    public final static class NonMathingPatternStringException extends Exception {
        public final static String messageFormatter =
                "Expected string matching %s but found: %s";

        private final String string;
        private final String pattern;

        private NonMathingPatternStringException(
                final String pattern,
                final String string) {
            super(String.format(messageFormatter, pattern, string));
            this.pattern = pattern;
            this.string = string;
        }

        public static NonMathingPatternStringException of(
                final String pattern,
                final String string) {
            return new NonMathingPatternStringException(pattern, string);
        }

        public String getPattern() {
            return pattern;
        }
        public String getString() {
            return string;
        }
    }
}
