package examples.samples.from;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;

public class From2 {

    public From2(
            final FieldType1 firstValue,
            final FieldType2 secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    private FieldType1 firstValue;

    private FieldType2 secondValue;

    public FieldType1 getFirstValue() {
        return firstValue;
    }

    public FieldType2 getSecondValue() {
        return secondValue;
    }
}
