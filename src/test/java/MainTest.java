import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.cli.Main;

public class MainTest {
    @Test
    public void handleInputTest() {
        String s = "demo";
        String temp = s + "\n";
        InputStream in = new ByteArrayInputStream(temp.getBytes());
        System.setIn(in);
        String result = Main.handleInput("test");
        System.setIn(System.in);
        assertEquals(s, result);
    }  
}
