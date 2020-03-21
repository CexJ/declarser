package examples.samples.from;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;

public class From3 {

    public From3(FieldType1 firstValue, FieldType2 secondValue, FieldType2 thirdValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
    }

    private FieldType1 firstValue;

    private FieldType2 secondValue;

    private FieldType2 thirdValue;

    public FieldType1 getFirstValue() {
        return firstValue;
    }

    public FieldType2 getSecondValue() {
        return secondValue;
    }

    public FieldType2 getThirdValue() {
        return thirdValue;
    }
}
