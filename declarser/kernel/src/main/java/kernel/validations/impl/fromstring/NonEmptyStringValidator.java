package kernel.validations.impl.fromstring;

import kernel.parsers.fromstring.tonumber.BigDecimalParser;
import kernel.validations.Validator;

import java.util.Optional;

public class NonEmptyStringValidator implements Validator<String> {

    private static class InstanceHolder {
        private static final NonEmptyStringValidator instance = new NonEmptyStringValidator();
    }

    public static NonEmptyStringValidator getInstance() {
        return NonEmptyStringValidator.InstanceHolder.instance;
    }

    private NonEmptyStringValidator(){}


    @Override
    public Optional<? extends Exception> apply(String s) {
        return s!=null && s.isEmpty() ? Optional.of(EmptyStringException.of(s))
                                      : Optional.empty();
    }

    public final static class EmptyStringException extends Exception {
        public final static String messageFormatter =
                "Expected non-empty string but found: %s";

        private final String string;

        private EmptyStringException(String string) {
            super(String.format(messageFormatter, string));
            this.string = string;
        }

        public static EmptyStringException of(String string) {
            return new EmptyStringException(string);
        }


        public String getString() {
            return string;
        }
    }
}
