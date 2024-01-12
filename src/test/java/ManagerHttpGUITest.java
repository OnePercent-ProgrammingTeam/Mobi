/* 
//import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gr.aueb.dmst.onepercent.programming.ManagerHttpGUI;

public class ManagerHttpGUITest {
    ManagerHttpGUI managerHttp = new ManagerHttpGUI();
    TestsHelper obj = TestsHelper.getInstance();
    @BeforeEach
    void setUp() {
        String input = obj.getTestid() + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        managerHttp = new ManagerHttpGUI();
    }
    @Test
    public void startContainerTest() {
        managerHttp.startContainer();
        assertTrue(
            obj.checkContainerStatus(obj.getTestid()));
    }
    //TO DO: check this method 
   /*  @Test
    public void stopContainerTest() {
        managerHttp.stopContainer();
        assertFalse(
            obj.checkContainerStatus(obj.getTestid()));
    
    }
    */


