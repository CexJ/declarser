package impl.stages.stage02_totypedmap.functions.fromString.toprimitives;

import utils.tryapi.Try;

import java.util.function.Function;

public class FromStringToDouble implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToDouble instance = new FromStringToDouble();
    }

    public static FromStringToDouble getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToDouble(){}


    @Override
    public Try<Double> apply(String s) {
        return Try.go(() -> Double.parseDouble(s));
    }
}
