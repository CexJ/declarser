package examples.samples.from;

import examples.samples.fields.FieldType1;
import examples.samples.fields.FieldType2;

public class From1 {

    public From1(
            final FieldType1 firstValue) {
        this.firstValue = firstValue;
    }

    private FieldType1 firstValue;

    public FieldType1 getFirstValue() {
        return firstValue;
    }

}
