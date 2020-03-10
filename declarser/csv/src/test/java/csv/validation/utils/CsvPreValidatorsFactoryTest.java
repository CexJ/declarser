package csv.validation.utils;

import csv.validation.utils.utils.*;
import kernel.exceptions.GroupedException;
import kernel.validations.Validator;
import kernel.validations.impl.PreValidator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;



public class CsvPreValidatorsFactoryTest {

    /*
     * GIVEN a string S1
     *  AND a string S2
     *  AND an Exception E
     *  AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
     *  AND PRMi values of String[] for i in (1,..,N)
     *  AND Fi valuee of type Function<String[], Validator<String>> for i in (1,..,N) such that
     *      for each i => Fi(PRMi)(VS) is Empty
     *      and there exists an j => Fj(PRMj)(VS) is Optional of E
     *  AND a map M with the entry Ci -> Fi
     *  AND a map CM
     *  AND a CsvPreValidatorFactory PVF constructed with M and CM
     * WHEN the method function is invoked with the list PreValidators of Ci and PRMi
     * THEN the result is a success
     *  AND the value is a validator V such that
     *      for V(S1) is Empty
     *      and V(S2) is Optional of E
     */
    @Test
    public void function_of_default_validators_is_a_success(){
        // GIVEN a string S1
        final var string1 = Constants.string1;
        // AND a string S2
        final var string2 = Constants.string2;
        // AND an Exception E
        final var exception = Constants.exception;
        // AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
        final var clazz1 = NoArgsValidatorClass1.class;
        final var clazz2 = NoArgsValidatorClass2.class;
        // AND PRMi values of String[] for i in (1,..,N)
        final var params1 = new String[]{string1, string2};
        final var params2 = new String[]{string1, string2};
        // AND Fi value of type Function<String[], Validator<String>> for i in (1,..,N) such that
        //     for each i => Fi(PRMi)(S1) is Empty
        //     and there exists an j => Fj(PRMj)(S2) is Optional of E
        final Function<String[], Validator<String>> function1 = ArgsValidatorClass1::new;
        final Function<String[], Validator<String>> function2 = ArgsValidatorClass2::new;
        // AND a map M with the entry Ci -> Fi
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        map.put(clazz1, function1);
        map.put(clazz2, function2);
        // AND a map CM
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        // AND a CsvPreValidatorFactory PVF constructed with M and CM
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list Ci
        final var inputList = Arrays.asList(PreValidator.of(clazz1, params1), PreValidator.of(clazz2, params2));
        final var result = csvPreValidatorFactory.function(inputList);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a validator V such that
        final var value = result.getValue();
        //     for V(S1) is Empty
        assertTrue(value.apply(string1).isEmpty());
        //     and V(S2) is Optional of E
        assertTrue(value.apply(string2).isPresent());
        final var exceptionResult = value.apply(string2).get();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a string S1
     *  AND a string S2
     *  AND an Exception E
     *  AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
     *  AND PRMi values of String[] for i in (1,..,N)
     *  AND Fi valuee of type Function<String[], Validator<String>> for i in (1,..,N) such that
     *      for each i => Fi(PRMi)(VS) is Empty
     *      and there exists an j => Fj(PRMj)(VS) is Optional of E
     *  AND a map M
     *  AND a map CM with the entry Ci -> Fi
     *  AND a CsvPreValidatorFactory PVF constructed with M and CM
     * WHEN the method function is invoked with the list PreValidators of Ci and PRMi
     * THEN the result is a success
     *  AND the value is a validator V such that
     *      for V(S1) is Empty
     *      and V(S2) is Optional of E
     */
    @Test
    public void function_of_custom_validators_is_a_success(){
        // GIVEN a string S1
        final var string1 = Constants.string1;
        // AND a string S2
        final var string2 = Constants.string2;
        // AND an Exception E
        final var exception = Constants.exception;
        // AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
        final var clazz1 = NoArgsValidatorClass1.class;
        final var clazz2 = NoArgsValidatorClass2.class;
        // AND PRMi values of String[] for i in (1,..,N)
        final var params1 = new String[]{string1, string2};
        final var params2 = new String[]{string1, string2};
        // AND Fi value of type Function<String[], Validator<String>> for i in (1,..,N) such that
        //     for each i => Fi(PRMi)(S1) is Empty
        //     and there exists an j => Fj(PRMj)(S2) is Optional of E
        final Function<String[], Validator<String>> function1 = ArgsValidatorClass1::new;
        final Function<String[], Validator<String>> function2 = ArgsValidatorClass2::new;
        // AND a map M with the entry Ci -> Fi
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        // AND a map CM
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        customMap.put(clazz1, function1);
        customMap.put(clazz2, function2);
        // AND a CsvPreValidatorFactory PVF constructed with M and CM
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list Ci
        final var inputList = Arrays.asList(PreValidator.of(clazz1, params1), PreValidator.of(clazz2, params2));
        final var result = csvPreValidatorFactory.function(inputList);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a validator V such that
        final var value = result.getValue();
        //     for V(S1) is Empty
        assertTrue(value.apply(string1).isEmpty());
        //     and V(S2) is Optional of E
        assertTrue(value.apply(string2).isPresent());
        final var exceptionResult = value.apply(string2).get();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a string S1
     *  AND a string S2
     *  AND PRMi values of String[] for i in (1,..,N)
     *  AND an Exception E
     *  AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N) with a no arguments constructor such that
     *      for each i => Ci(PRMi)(S1) is Empty
     *      and there exists an j => Fj(PRMj)(S2) is Optional of E
     *  AND a CsvPreValidatorFactory PVF
     * WHEN the method function is invoked with the list PreValidators of Ci
     * THEN the result is a success
     *  AND the value is a validator V such that
     *      for V(S1) is Empty
     *      and V(S2) is Optional of E
     */
    @Test
    public void function_of_no_argument_constructor_validators_is_a_success(){
        // GIVEN a string S1
        final var string1 = Constants.string1;
        // AND a string S2
        final var string2 = Constants.string2;
        // AND an Exception E
        final var exception = Constants.exception;
        // AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
        final var clazz1 = NoArgsValidatorClass1.class;
        final var clazz2 = NoArgsValidatorClass2.class;
        // AND a CsvPreValidatorFactory PVF
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list Ci
        final var inputList = Arrays.asList(PreValidator.of(clazz1, new String[]{}), PreValidator.of(clazz2, new String[]{}));
        final var result = csvPreValidatorFactory.function(inputList);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a validator V such that
        final var value = result.getValue();
        //     for V(S1) is Empty
        assertTrue(value.apply(string1).isEmpty());
        //     and V(S2) is Optional of E
        assertTrue(value.apply(string2).isPresent());
        final var exceptionResult = value.apply(string2).get();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a string S1
     *  AND a string S2
     *  AND an Exception E
     *  AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N) with a String[] arguments constructor such that
     *      for each i => Ci(PRMi)(S1) is Empty
     *      and there exists an j => Fj(PRMj)(S2) is Optional of E
     *  AND a CsvPreValidatorFactory PVF
     * WHEN the method function is invoked with the list PreValidators of Ci PRMi
     * THEN the result is a success
     *  AND the value is a validator V such that
     *      for V(S1) is Empty
     *      and V(S2) is Optional of E
     */
    @Test
    public void function_of_argument_constructor_validators_is_a_success(){
        // GIVEN a string S1
        final var string1 = Constants.string1;
        // AND a string S2
        final var string2 = Constants.string2;
        // AND an Exception E
        final var exception = Constants.exception;
        // AND Ci values of type Class<? extends Validator<String>> for i in (1,..,N)
        final var clazz1 = ArgsValidatorClass1.class;
        final var clazz2 = ArgsValidatorClass2.class;
        // AND PRMi values of String[] for i in (1,..,N)
        final var params1 = new String[]{string1, string2};
        final var params2 = new String[]{string1, string2};
        // AND a CsvPreValidatorFactory PVF
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list Ci
        final var inputList = Arrays.asList(PreValidator.of(clazz1, params1), PreValidator.of(clazz2, params2));
        final var result = csvPreValidatorFactory.function(inputList);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is a validator V such that
        final var value = result.getValue();
        //     for V(S1) is Empty
        assertTrue(value.apply(string1).isEmpty());
        //     and V(S2) is Optional of E
        assertTrue(value.apply(string2).isPresent());
        final var exceptionResult = value.apply(string2).get();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN C values of type Class<? extends Validator<String>> without a String[] arguments or a no arguments constructor
     *  AND a map M without C
     *  AND a map CM without C
     *  AND a CsvPreValidatorFactory PVF constructed with M and CM
     * WHEN the method function is invoked with the list PreValidators of C
     * THEN the result is a failure
     *  AND the exception is of type GroupedException
     *  AND the groupedException contains one ClassNotFoundException
     */
    @Test
    public void function_of_invalid_constructor_validators_is_a_failure(){
        // GIVEN C values of type Class<? extends Validator<String>> without a String[] arguments or a no arguments constructor
        final var clazz1 = NoValidConstructorValidatorClass1.class;
        // AND a map M without C
        final var map = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        // AND a map CM without C
        final var customMap = new HashMap<Class<? extends Validator<String>>, Function<String[], Validator<String>>>();
        // AND a CsvPreValidatorFactory PVF constructed with M and CM
        final var csvPreValidatorFactory = CsvPreValidatorsFactory.of(map, customMap);
        // WHEN the method function is invoked with the list PreValidators of C
        final var inputList = Collections.singletonList(PreValidator.of(clazz1, new String[]{}));
        final var result = csvPreValidatorFactory.function(inputList);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of type GroupedException
        final var exception = result.getException();
        assertEquals(exception.getClass(), GroupedException.class);
        // AND the groupedException contains one ClassNotFoundException
        final var groupedException = (GroupedException) exception;
        assertEquals(groupedException.getExceptions().size(),1);
        final var classNotFoundException = groupedException.getExceptions().get(0);
        assertEquals(classNotFoundException.getClass(),ClassNotFoundException.class);
    }
}
