package csv.stages.stage01_tomap.exceptions;

public final class CsvNullInputException extends Exception {

    public static String message =
            "I was expecting a String to parse but I find null";

    private CsvNullInputException() {
        super(message);
    }

    public static CsvNullInputException getInstance() {
        return new CsvNullInputException();
    }
}
