package kernel.stages.stage04_toobject.impl.exceptions;

import java.util.Map;

public final class OutputGluingException extends Exception {

    public final static String messageFormatter =
            "%s while gluing the following map: %s";

    private final Map<?,?> map;

    private OutputGluingException(
            final Map<?,?> map, Exception ex) {
        super(String.format(messageFormatter, ex.getMessage(), map.toString()),ex);
        this.map = map;
    }

    public static OutputGluingException of(
            final Map<?,?> map, Exception ex) {
        return new OutputGluingException(map, ex);
    }

    public Map<?, ?> getMap() {
        return map;
    }
}
