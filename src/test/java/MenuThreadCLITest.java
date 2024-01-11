import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import gr.aueb.dmst.onepercent.programming.MenuThreadCLI;

public class MenuThreadCLITest {
    MenuThreadCLI obj = new MenuThreadCLI();

    @Test
    public void testHandleUserInput() {
        String s = "test\n";
        InputStream in = new ByteArrayInputStream(s.getBytes());
        System.setIn(in);
        assertTrue(obj.handleUserInput()); //check input mismatch case

        s = "1\nY\n";
        in = new ByteArrayInputStream(s.getBytes()); 
        //check the case that user gives number and press Y  
        System.setIn(in);
        assertTrue(obj.handleUserInput());

        // if user presses number and N there is no return statement as the app closes 
        //so we cannot test this case 
    }
    
}
