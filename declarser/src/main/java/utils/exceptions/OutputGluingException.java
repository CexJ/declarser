package utils.exceptions;

import java.util.Map;

public class OutputGluingException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while gluing the following map: %s";

    private final Map<?,?> map;

    private OutputGluingException(Map<?,?> map, Exception ex) {
        super(String.format(messageFormatter, ex.toString(), map.toString()),ex);
        this.map = map;
    }

    public static OutputGluingException of(Map<?,?> map, Exception ex) {
        return new OutputGluingException(map, ex);
    }
}
