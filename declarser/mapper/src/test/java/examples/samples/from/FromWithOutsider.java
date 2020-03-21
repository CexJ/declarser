package examples.samples.from;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;

public class FromWithOutsider {

    public FromWithOutsider(
            final FieldType1 firstValue,
            final FieldType2 secondValue) {
        this.firstValue = firstValue;
        this.fromOutsider = secondValue;
    }

    private FieldType1 firstValue;

    private FieldType2 fromOutsider;

    public FieldType1 getFirstValue() {
        return firstValue;
    }

    public FieldType2 getFromOutsider() {
        return fromOutsider;
    }
}
