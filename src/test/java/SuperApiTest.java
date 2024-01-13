import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.SuperAPI;


public class SuperApiTest {
    @Test
    public void testCreateDockerClient() {
        SuperAPI.createDockerClient();
        assertNotNull(SuperAPI.getDc());
        

    }
    
}
