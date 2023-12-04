import com.fasterxml.jackson.core.JsonProcessingException;

import gr.aueb.dmst.onepercent.programming.ContainerMonitorHttp;
import gr.aueb.dmst.onepercent.programming.ContainerHttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestContainerMonitorHttp {

    private ContainerMonitorHttp containerMonitor;

    @BeforeEach
    public void setUp() {
        containerMonitor = new ContainerMonitorHttp();
    }

    @Test
    public void testGetFormattedStats() {
        StringBuffer responseBuffer = new StringBuffer("{ \"cpu_stats\": { \"cpu_usage\": { \"total_usage\": 100 }, \"system_cpu_usage\": 200, \"online_cpus\": 4 }, \"precpu_stats\": { \"cpu_usage\": { \"total_usage\": 50 }, \"system_cpu_usage\": 100 } } }");

        try {
            double result = containerMonitor.getFormattedStats(responseBuffer);
            assertEquals(200.0, result);
        } catch (JsonProcessingException e) {
            fail("Exception not expected", e);
        }
    }

    @Test
    public void testPrepareStorageData() {
        
        ContainerHttpRequest.containerId = "eae733773feefb6adbbb2226c20eb9125adbe4d1fb69da2b1cafd6304eb11c8c";
        ContainerHttpRequest.response1 = new StringBuffer("{ \"Name\": \"/optimistic_cerf\", \"Id\": \"eae733773feefb6adbbb2226c20eb9125adbe4d1fb69da2b1cafd6304eb11c8c\", \"NetworkSettings\": { \"Networks\": { \"bridge\": { \"IPAddress\": \"172.17.0.2\", \"MacAddress\": \"02:42:ac:11:00:02\" } } }, \"cpu_stats\": { \"cpu_usage\": { \"total_usage\": 100 }, \"system_cpu_usage\": 200, \"online_cpus\": 4 }, \"precpu_stats\": { \"cpu_usage\": { \"total_usage\": 50 }, \"system_cpu_usage\": 100 } } }");

        try {
            String[] result = containerMonitor.prepareStoragedData();
            assertNotNull(result);
            assertEquals(6, result.length);
            assertEquals("optimistic_cerf", result[0]);
            assertEquals("eae733773feefb6adbbb2226c20eb9125adbe4d1fb69da2b1cafd6304eb11c8c", result[1]);
            assertEquals("172.17.0.2", result[2]);
            assertEquals("02:42:ac:11:00:02", result[3]);
            //assertEquals("200.0", result[4]); 
            //we can not check the real time date and time 
        } catch (Exception e) {
            fail("Exception not expected", e);
        }
    }
}