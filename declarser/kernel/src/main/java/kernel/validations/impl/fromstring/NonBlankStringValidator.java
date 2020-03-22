package kernel.validations.impl.fromstring;

import kernel.validations.Validator;

import java.util.Optional;

public final class NonBlankStringValidator implements Validator<String> {

    private static class InstanceHolder {
        private static final NonBlankStringValidator instance = new NonBlankStringValidator();
    }

    public static NonBlankStringValidator getInstance() {
        return NonBlankStringValidator.InstanceHolder.instance;
    }

    private NonBlankStringValidator(){}


    @Override
    public Optional<? extends Exception> apply(
            final String s) {
        return s==null || s.isBlank() ? Optional.of(BlankStringException.of(s))
                                      : Optional.empty();
    }

    public final static class BlankStringException extends Exception {
        public final static String messageFormatter =
                "Expected non-blank string but found: %s";

        private final String string;

        private BlankStringException(
                final String string) {
            super(String.format(messageFormatter, string));
            this.string = string;
        }

        public static BlankStringException of(
                final String string) {
            return new BlankStringException(string);
        }


        public String getString() {
            return string;
        }
    }
}
