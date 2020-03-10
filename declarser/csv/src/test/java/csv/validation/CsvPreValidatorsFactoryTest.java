package csv.validation;

import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.validations.Validator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class CsvPreValidatorsFactoryTest {

    private final static class ValidatorClass1 implements Validator<String> {
        @Override
        public Optional<? extends Exception> apply(String s) {
            return Optional.empty();
        }
    }

    private final static class ValidatorClass2 implements Validator<String> {
        @Override
        public Optional<? extends Exception> apply(String s) {
            return Optional.empty();
        }
    }

    /*
     * GIVEN a string S1
     *  AND a string S2
     *  AND a String[] PRM
     *  AND an Exception E
     *  AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
     *  AND Fi value of type Function<String[], Validator<String>> for i in (1,..,N) such that
     *      for each i => Fi(PRM)(VS) is Empty
     *      and there exists an j => Fi(PRM)(VS) is Optional of E
     *  AND a map M with the entry Ci -> Fi
     *  AND a map CM
     *  AND a CsvPreValidatorFactory PVF constructed with M and CM
     * WHEN the method function is invoked with the list Ci
     * THEN the result is a success
     *  AND the value is a validator V such that
     *      for V(S1) is Empty
     *      and V(S2) is Optional of E
     */

    @Test
    public void test(){
        // GIVEN a string S1
        final var stirng1 = "valid string";
        // AND a string S2
        final var stirng2 = "invalid string";
        // AND a String[] PRM
        final var params = new String[]{"param1", "param2"};
        // AND an Exception E
        final var exception = new Exception();
        // AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
        final var clazz1 = ValidatorClass1.class;
        final var clazz2 = ValidatorClass2.class;
        // AND Fi value of type Function<String[], Validator<String>> for i in (1,..,N) such that
        //     for each i => Fi(PRM)(S1) is Empty
        //     and there exists an j => Fi(PRM)(S2) is Optional of E
        final Function<String[], Validator<String>> function1 = prm ->
                s -> stirng1.equals(s) ? Optional.empty() :
                     stirng2.equals(s) ? Optional.empty() :
                                         Optional.of(exception);
        final Function<String[], Validator<String>> function2 = prm ->
                s -> stirng1.equals(s)   ? Optional.empty() :
                     ! stirng2.equals(s) ? Optional.empty() :
                                           Optional.of(exception);
        // AND a map M with the entry Ci -> Fi
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        map.put(clazz1, function1);
        map.put(clazz2, function2);
        // AND a map CM
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        // AND a CsvPreValidatorFactory PVF constructed with M and CM
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list Ci
        csvPreValidatorFactory.function(Arrays.asList(clazz1, clazz2));
        // THEN the result is a success
        // AND the value is a validator V such that
        //     for V(S1) is Empty
        //     and V(S2) is Optional of E
    }

}
