import gr.aueb.dmst.onepercent.programming.ExecutorThread;
import gr.aueb.dmst.onepercent.programming.MonitorHttp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Class: MonitorThreadTest is used to test the methods of MonitorThread class **/
public class MonitorThreadTest {

    MonitorHttp containerMonitorHttp = new MonitorHttp();
    ExecutorThread executorThread = new ExecutorThread();

    /** Method: testsearchImages() tests if the user input is 3*/    
    @Test
    public void testsearchImages() {
        executorThread.setUserInput(3);
        assertEquals(3, executorThread.getUserInput());
    }
    
    /** Method: testexecuteDiagram() tests if the user input is 5*/    
    @Test
    public void testexecuteDiagram() {
        executorThread.setUserInput(5);
        assertEquals(5, executorThread.getUserInput());
    }

    /** Method: testinspectContainer() tests if the user input is 6*/    
    @Test
    public void testinspectContainer() {
        executorThread.setUserInput(6);
        assertEquals(6, executorThread.getUserInput());
    }

    /** Method: teststartSavingData() tests if the user input is 7*/    
    @Test
    public void teststartSavingData() {
        executorThread.setUserInput(7);
        assertEquals(7, executorThread.getUserInput());
    }
    
    /** Method: testcontainerMonitor() tests if the user input is 8*/    
    @Test
    public void testcontainerMonitor() {
        executorThread.setUserInput(8);
        assertEquals(8, executorThread.getUserInput());
    }
}
