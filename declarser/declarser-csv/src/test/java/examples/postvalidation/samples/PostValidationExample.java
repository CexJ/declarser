package examples.postvalidation.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;

public class PostValidationExample {

    @CsvColumn(0)
    private Integer subQuantity1;

    @CsvColumn(1)
    private Integer subQuantity2;

    @CsvColumn(2)
    private Integer totalQuantity;

    public Integer getSubQuantity1() {
        return subQuantity1;
    }

    public Integer getSubQuantity2() {
        return subQuantity2;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }
}
