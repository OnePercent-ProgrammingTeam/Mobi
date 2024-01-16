import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI;

public class MonitorThreadGUITest {
    @Test
      public void testGetInstance() {
        MonitorThreadGUI obj = MonitorThreadGUI.getInstance();
        assertNotNull(obj);
        MonitorThreadGUI obj2 = MonitorThreadGUI.getInstance();
        assertEquals(obj, obj2);

    }
    
}
