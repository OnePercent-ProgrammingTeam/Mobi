package gr.aueb.dmst.onepercent.programming.core;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.time.LocalDate;
import java.time.LocalTime;


/** Import the Jackson ObjectMapper class for formatting the JSON response.*/
import com.fasterxml.jackson.databind.ObjectMapper; 
/** Import the Jackson JsonNode class for formatting the JSON response.*/
import com.fasterxml.jackson.databind.JsonNode; 
/** Import the Jackson JsonProcessingException for handling an exception that might occur. */
import com.fasterxml.jackson.core.JsonProcessingException; 

/** Class: MonitorHttp is responsible for the http requests that are made to the docker daemon
 *  in order to get information about a container, get statistics about a running container
 *  or search for an image.
 *  It extends the SuperHttp class.
 *  @see SuperHttp
 */
public abstract class MonitorHttp extends SuperHttp {

    /**
     * Abstract method to inspect details of a container.
     * Implementation should provide the specific logic for inspecting container information.
     */
    public abstract void inspectContainer();

    /**
     * Abstract method to retrieve statistics for a container.
     *
     * @param MIGHT_GOT_TO_REMOVE_PARAMETER The ID of the container 
     * for which statistics are requested.
     * 
     * @return CloseableHttpResponse containing container statistics.
     *         Implementation should handle the specific logic for obtaining container stats.
     */
    public abstract CloseableHttpResponse getContainerStats(String MIGHT_GOT_TO_REMOVE_PARAMETER);

    /**
     * Abstract method to search for Docker images.
     * Implementation should provide the specific logic for searching images.
     */
    public abstract void searchImage();
    

    /** Method: getHttpResponse(String) executes the http request for getting 
     *  stats about a running container.
     * throws Exception if an error occurs while executing the http request.
     * @return response
     */
    @Override
    public CloseableHttpResponse getHttpResponse() {
        try {
            CloseableHttpResponse response = MonitorHttp.HTTP_CLIENT.execute(getRequest);

            //int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("Status Code : " + statusCode);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage()); // Print the error message
        } 
        return null;
    }

    /** Method: getFormattedStats(StringBuffer) formats the json response for stats 
     *  to a user-friendly message.
     *  that is real-time updated and printed to the console
     *  @param response1Buffer a StringBuffer object.
     *  @throws JsonProcessingException if an error occurs while executing the http request.
     *
     *  The code below should work too. Instead, it return 0.0. We have to check it further
     *      
     *  <code>
     *  double cpu_delta = jsonNode.get("cpu_stats").get("cpu_usage").get("total_usage").asDouble()
     *                - jsonNode.get("precpu_stats").get("cpu_usage").get("total_usage").asDouble();
     *  double system_delta = jsonNode.get("cpu_stats").get("system_cpu_usage").asDouble()
     *                      - jsonNode.get("precpu_stats").get("system_cpu_usage").asDouble();
     *  double number_cpus = jsonNode.get("cpu_stats").get("online_cpus").asDouble();
     *
     *  return (system_delta!=0.0)? (cpu_delta / system_delta) * number_cpus * 100.0 : 0.0;
     *  </code>
     * 
     * @return cpu usage
     */
    
    public double getCPUusage(StringBuilder response1Buffer) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1Buffer.toString());
            double  cpu_delta = jsonNode.at("/cpu_stats/cpu_usage/total_usage").asDouble()
                            - jsonNode.at("/precpu_stats/cpu_usage/total_usage").asDouble();
            
            double system_delta = jsonNode.at("/cpu_stats/system_cpu_usage").asDouble()
                                - jsonNode.at("/precpu_stats/system_cpu_usage").asDouble();
            
            double number_cpus = jsonNode.at("/cpu_stats/online_cpus").asDouble();
            if (system_delta == 0) {
                System.out.println("\n\nSomething went wrong.\n"
                                 + "The formula to calculate CPU usage is:\n"
                                 + "(cpu_delta / system_delta) * number_cpus * 100.0\n"
                                 + "and system_delta is 0.\n" 
                                 + "Make sure that the container is running");
            }
            double cpuUsage = (cpu_delta / system_delta) * number_cpus * 100.0;
            return cpuUsage;

        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
        return 0.0;
    }

    /*
     * Formats the json response for memory stats to a user-friendly message.
     */
    public double getMemoryUsage(StringBuilder response1Buffer) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1Buffer.toString());
            double  used_memory = jsonNode.at("/memory_stats/usage").asDouble()
                            - jsonNode.at("/memory_stats/stats/cache").asDouble();
            
            double available_memory = jsonNode.at("/memory_stats/limit").asDouble();
                                
            if (available_memory == 0) {
                //TO DO: Make customised exception
                System.out.println("\n\nSomething went wrong.\n"
                                 + "The formula to calculate Memory usage is:\n"
                                 + "(used_memory / available_memory) * 100.0\n"
                                 + "and available_memory is 0.\n" 
                                 + "Make sure that the container is running");
            }
            double memory_usage = (used_memory / available_memory) * 100.0;
            return memory_usage;

        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
        return 0.0;
    }

    /**
     * Method: getTableforContainer() retrieves information about a container
     * and formats it into an array for table representation.
     * 
     * @return An array containing container information:
     *         [0] Container ID
     *         [1] Container Name
     *         [2] Container Status
     *         [3] Image ID
     *         [4] Network ID
     *         [5] Gateway
     *         [6] IP Address
     *         [7] Mac Address
     * @throws JsonProcessingException if there is an error processing the JSON response.
     */
    public String[] getTableforContainer() throws JsonProcessingException {
        String[] info = new String[8];
        try {
            getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                    "/containers/" + 
                                    MonitorHttp.containerId + 
                                    "/json");
            executeHttpRequest("prepare storage");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());

            info[0] = jsonNode.get("Id").asText();
            info[1] = jsonNode.get("Name").asText().substring(1);
            info[2] = jsonNode.get("State").get("Status").asText();
            info[3] = jsonNode.get("Image").asText();
            info[4] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("NetworkID")
                            .asText();
            info[5] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("Gateway")
                            .asText();
            info[6] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("IPAddress")
                            .asText();
            info[7] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("MacAddress")
                            .asText();

        } catch (NullPointerException e) {
            System.out.println("Exception due to null value");
        }
        return info;

    }

    //TO DO: It's Kiritsai's, might be deleted
    /** Method: prepareStorageData() returns a String array with the prepared 
     *  data that will be saved in a csv file. 
     *  @return str a String array with the data that will be saved in a csv file.
     *  @throws NullPointerException if an error occurs while executing the http request.
     */
    public String[] prepareStoragedData() {
        try {
            getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                    "/containers/" + 
                                    MonitorHttp.containerId + 
                                    "/json");
            executeHttpRequest("prepare storage");
            String[] str = new String[6];

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            
            str[0] = jsonNode.at("/Name").asText().substring(1); //Container Name
            // We use substring() in order to ignore the "/" from the container name
            str[1] = jsonNode.at("/Id").asText(); //Container ID
            str[2] = jsonNode
                     .at("/NetworkSettings/Networks/bridge/IPAddress")
                     .asText(); //IP Address
            str[3] = jsonNode
                    .at("/NetworkSettings/Networks/bridge/MacAddress")
                    .asText(); //Mac Address

            SuperHttp.getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                              "/containers/" + 
                                              MonitorHttp.containerId + 
                                              "/stats");
            executeHttpRequest("stats");
            
            str[4] = String.valueOf(lastCPUUsage); //CPU Usage
            LocalDate date = LocalDate.now(); 
            LocalTime time = LocalTime.now();

            //Date and Time in one
            str[5] = date.toString() + " " + time.toString().substring(0, 8); 
            
            return str;
          
        } catch (Exception e) {
            System.out.println("Exception while preparing storage of data");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Default Constructor
     */
    public MonitorHttp() {

    }
}
