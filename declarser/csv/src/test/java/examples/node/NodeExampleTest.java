package examples.node;

import csv.CsvDeclarserFactory;
import examples.node.samples.NodeExample;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeExampleTest {

    @Test
    public void parse_valid_input_node_return_success() {
        final var csv = "string;10 20";
        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var tryDeclarser = declarserFactory.declarserOf(NodeExample.class,  ";");
        assertTrue(tryDeclarser.isSuccess());
        final var declarser = tryDeclarser.getValue();
        final var result = declarser.parse(csv);
        assertTrue(result.isSuccess());
        final var value = result.getValue();
        assertEquals(value.getaString(),"string");
        assertEquals(value.getaSubNodeExample().getFirstInteger(),10);
        assertEquals(value.getaSubNodeExample().getSecondInteger(),20);
    }
}
