import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.ExecutorThreadGUI;

public class ExecutorThreadGUITest {
    @Test
    public void getInstanceTest() {
        ExecutorThreadGUI obj = ExecutorThreadGUI.getInstance();
        assertNotNull(obj);
        assertEquals(obj, ExecutorThreadGUI.getInstance());

    }
}
