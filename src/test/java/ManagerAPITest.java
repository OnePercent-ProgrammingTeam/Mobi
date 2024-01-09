import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.ManagerAPI;

public class ManagerAPITest {
    ManagerAPI obj = new ManagerAPI();
    @Test
    void handleInputTest() {
        obj.createDockerClient();
        String id = "91ae36fe661b1b81b588a68efee2acb162584e46dd5cf91080abddd581a9bfac";
        String temp = id + "\n";
        InputStream in = new ByteArrayInputStream(temp.getBytes());
        System.setIn(in);
        String result = obj.handleInput();
        System.setIn(System.in);
        assertEquals(id, result);
    }
    @Test
    void checkActiveContainerStatus() {
        obj.createDockerClient();
        String id = "91ae36fe661b1b81b588a68efee2acb162584e46dd5cf91080abddd581a9bfac";
        boolean result = obj.checkActiveContainerStatus(id);
        assertFalse(result);
    }
    @Test
    void checkAllContainerStatus() {
        obj.createDockerClient();
        String id = "91ae36fe661b1b81b588a68efee2acb162584e46dd5cf91080abddd581a9bfac";
        boolean result = obj.checkAllContainerStatus(id);
        assertTrue(result);
    }
}
