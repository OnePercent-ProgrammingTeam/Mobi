import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.MonitorThreadCLI;

public class MonitorThreadCLITest {
    @Test
    public void testGetInstance() {
        MonitorThreadCLI obj = MonitorThreadCLI.getInstance();
        assertNotNull(obj);
        MonitorThreadCLI obj2 = MonitorThreadCLI.getInstance();
        assertEquals(obj, obj2);

    }






    
}
