import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.ExecutorThreadCLI;

public class ExecutorThreadCLITest {
    @Test 
    public void getInstanceTest() {
        ExecutorThreadCLI obj = ExecutorThreadCLI.getInstance();
        assertNotNull(obj);
        assertEquals(obj, ExecutorThreadCLI.getInstance());
    }
}
