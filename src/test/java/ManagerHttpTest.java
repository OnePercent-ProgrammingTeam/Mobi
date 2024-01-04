import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.ManagerHttp;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;


class ManagerHttpTest {
    private ManagerHttp managerHttp;

    @BeforeEach
    void setUp() {

        String input = "91ae36fe661b1b81b588a68efee2acb162584e46dd5cf91080abddd581a9bfac\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        managerHttp = new ManagerHttp();
    }

    @Test
     void testHandleOutput() {
        String result1 = managerHttp.handleOutput("start");
        assertEquals(result1, "response has not been initialized");

    }

}
