package kernel.validations.impl.fromstring;

import kernel.validations.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StringPatternValidator implements Validator<String> {


    private final String pattern;

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
            final String format){
        this.pattern = format;
    }

    @Override
    public Optional<? extends Exception> apply(String s) {
        return s==null || !s.matches(pattern) ? Optional.of(NonMathingPatternStringException.of(pattern, s))
                                              : Optional.empty();
    }

    public final static class NonMathingPatternStringException extends Exception {
        public final static String messageFormatter =
                "Expected string matching %s but found: %s";

        private final String string;
        private final String pattern;

        private NonMathingPatternStringException(String pattern, String string) {
            super(String.format(messageFormatter, pattern, string));
            this.pattern = pattern;
            this.string = string;
        }

        public static NonMathingPatternStringException of(String pattern, String string) {
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
