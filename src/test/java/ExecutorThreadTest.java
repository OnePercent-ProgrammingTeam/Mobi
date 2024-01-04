import gr.aueb.dmst.onepercent.programming.ManagerHttp;
import gr.aueb.dmst.onepercent.programming.ExecutorThread;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Class: ExecutorThreadTest is used to test the methods of ExecutorThread class **/
public class ExecutorThreadTest {

    ManagerHttp containerManagerHttp = new ManagerHttp();
    ExecutorThread executorThread = new ExecutorThread();
   
    /** Method: teststartContainer() tests if the user input is 4*/    
    @Test
    public void teststartContainer() {
        executorThread.setUserInput(1);
        assertEquals(1, executorThread.getUserInput());
    }

    /** Method: teststopContainer() tests if the user input is 4*/
    @Test
    public void teststopContainer() {
        executorThread.setUserInput(2);
        assertEquals(2, executorThread.getUserInput());
    }

    /** Method: testpullImage() tests if the user input is 4*/
    @Test
    public void testpullImage() {
        executorThread.setUserInput(4);
        assertEquals(4, executorThread.getUserInput());
    }
}