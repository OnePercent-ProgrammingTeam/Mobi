/* import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dockerjava.api.model.Container;

import gr.aueb.dmst.onepercent.programming.ManagerAPI;

public class ManagerAPITest {
    ManagerAPI obj = new ManagerAPI();
    TestsHelper obj2 = TestsHelper.getInstance();
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
        String id = obj2.getTestid();
        boolean result = obj.checkActiveContainerStatus(id);
        assertEquals(result, obj2.checkContainerStatus(id));
    }
    @Test
    void checkAllContainerStatus() {
        obj.createDockerClient();
        String id = obj2.getTestid();
        boolean result = obj.checkAllContainerStatus(id);
        List<Container> arr = obj2.getAllContainers();
        boolean flug = false;
        for (Container c : arr) {
            if (c.getId().equals(id))
                flug = true;
        }
        assertTrue(flug);
    }
}

*/

