package examples.node.samples;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.annotations.fields.CsvNode;

public class NodeExample {

    @CsvColumn(0)
    private String aString;

    @CsvColumn(1)
    @CsvNode(" ")
    private SubNodeExample aSubNodeExample;

    public String getaString() {
        return aString;
    }

    public SubNodeExample getaSubNodeExample() {
        return aSubNodeExample;
    }
}
