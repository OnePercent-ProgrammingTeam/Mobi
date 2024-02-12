import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.core.Graph;

public class GraphTest {
    @Test
    public void getInstanceTest() {
        Graph obj = Graph.getInstance();
        assertNotNull(obj);
        assertEquals(obj, Graph.getInstance());
    }
}
