import com.fasterxml.jackson.core.JsonProcessingException;

import gr.aueb.dmst.onepercent.programming.core.Monitor;
import gr.aueb.dmst.onepercent.programming.cli.MonitorCLI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MonitorTest {

    TestsHelper obj = TestsHelper.getInstance();
    private MonitorCLI containerMonitor;

    @BeforeEach
    // Create a new instance of MonitorHttp before each test
    public void setUp() {
        containerMonitor = new MonitorCLI();
    }

    @Test
    public void testGetFormattedStats() {
        String s = "{ \"cpu_stats\": { \"cpu_usage\": { \"total_usage\": 100 }, "; 
        String s1 = " \"system_cpu_usage\": 200, \"online_cpus\": 4 }, ";
        String s2 = "\"precpu_stats\": { \"cpu_usage\": { \"total_usage\": 50 }, ";
        String s3 = "\"system_cpu_usage\": 100 } } }\"";
        StringBuilder responseBuffer = new StringBuilder(s + s1 + s2 + s3);

        try {
            // Test the getFormattedStats method and assert the result
            double result = containerMonitor.getCPUusage(responseBuffer);
            assertEquals(200.0, result);
        } catch (JsonProcessingException e) {
            fail("Exception not expected", e);
        }
    }

    @Test
       public void testPrepareStorageData() {
    // Test the testPrepareStorageData method and assert the result
        Monitor.containerId = obj.getTestid();
        
        try {
            String[] result = containerMonitor.prepareCsvStorageData();
            assertNotNull(result);
            assertEquals(6, result.length);
            assertEquals(obj.getTesContainer().getNames()[0].substring(1), result[0]);
            assertEquals(obj.getTesContainer().getId(), 
                result[1]);
        } catch (Exception e) {
            fail("Exception not expected", e);
        }
    }
}

