package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToBoolean implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToBoolean instance = new FromStringToBoolean();
    }

    public static FromStringToBoolean getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToBoolean(){}

    @Override
    public Try<Boolean> apply(String s) {
        return Try.go(() -> Boolean.parseBoolean(s));
    }
}
