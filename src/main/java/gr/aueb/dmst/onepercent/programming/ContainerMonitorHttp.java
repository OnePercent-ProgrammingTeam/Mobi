package gr.aueb.dmst.onepercent.programming;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;
import java.time.*;

/** Import the Jackson ObjectMapper class for formatting the JSON response*/
import com.fasterxml.jackson.databind.ObjectMapper; 
/** Import the Jackson JsonNode class for formatting the JSON response*/
import com.fasterxml.jackson.databind.JsonNode; 
/** Import the Jackson JsonProcessingException for handling an exception that might occur */
import com.fasterxml.jackson.core.JsonProcessingException; 

public class ContainerMonitorHttp extends ContainerHttpRequest {
    
     /** Get information about a container that might be or might not be locally installed
     *
     */
    public void getContainerInformation() {
        String message = "json"; // get the container statistics in json format
        ContainerManagerHttp.containerId = Test.handleInput(message);
        getRequest = new HttpGet(ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/" + message );
        System.out.println("Follow the link for " + message +" info:\n" + "LINK: " + ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/" + message + "\n\n");
        executeHttpRequest(message);
    }

    /** Get statistics about a running container sush as memory-CPU usage etc 
     * These stats are used in ContainerVisualization in order to create a graph
    */
    public CloseableHttpResponse getContainerStats() {
        String message = "stats"; // get the container statistics in json format
        ContainerManagerHttp.containerId = Test.handleInput(message);
        getRequest = new HttpGet(ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/" + message );
        System.out.println("Follow the link for " + message +" info:\n" + "LINK: " + ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/" + message + "\n\n");
        return getHttpResponse(message);
    }

    /** Search for an image by it's name. The result is limited to 3 */
    public void searchImages() {
        String message = "/images/search"; // get the container statistics in json format
        imageName = Test.handleInput("Please type the name of the image you want to search for: ");
        getRequest = new HttpGet(ContainerManagerHttp.DOCKER_HOST + message + "?term="+ imageName + "&limit=3" );
        System.out.println(ContainerManagerHttp.DOCKER_HOST + message + "?term="+ imageName + "&limit=3");
        executeHttpRequest(message);
    }

    /** Execute the http request for getting info about a container
     * @param message the final part of the url that is used to get the info
     * @throws Exception if an error occurs while executing the http request
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            CloseableHttpResponse response = ContainerManagerHttp.HTTP_CLIENT.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code : " + statusCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            ContainerMonitorHttp.response1 = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (message.equals("stats")){
                    lastCPUUsage = getFormattedStats(response1); //print real time CPU Usage
                    System.out.println("CPU Usage: " + lastCPUUsage);
                    response.close();
                    break;
                }
            }
            
            reader.close();
            
            if (message.equals("json")){
                printFormattedJson();
               // prepareStorageData(); 
            }
            if (message.equals("/images/search")){
                printFormattedJsonForImage();
            } 
                               
            } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } 
    }

    /** Execute the http request for getting stats abut a running container
     * @param message the final part of the url that is used to get the info
     * @throws Exception if an error occurs while executing the http request
     */
    @Override
    public CloseableHttpResponse getHttpResponse(String message) {
        try {
            CloseableHttpResponse response = ContainerManagerHttp.HTTP_CLIENT.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code : " + statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } 
        return null;
    }

    /** Format the json response for stats to a user-friendly message
     *  that is real-time updated and printed to the console
     *  @param response1Buffer a StringBuffer object
     *  @throws NullPointerException if an error occurs while executing the http request
     *
     *  The code below should work too. Instead, it return 0.0. We have to check it further
     *      
     *  <code>
     *  double cpu_delta = jsonNode.get("cpu_stats").get("cpu_usage").get("total_usage").asDouble()
     *                   - jsonNode.get("precpu_stats").get("cpu_usage").get("total_usage").asDouble();
     *  double system_delta = jsonNode.get("cpu_stats").get("system_cpu_usage").asDouble()
     *                      - jsonNode.get("precpu_stats").get("system_cpu_usage").asDouble();
     *  double number_cpus = jsonNode.get("cpu_stats").get("online_cpus").asDouble();
     *
     *  return (system_delta!=0.0)? (cpu_delta / system_delta) * number_cpus * 100.0 : 0.0;
     *  </code>
     */
    
     public double getFormattedStats(StringBuffer response1Buffer) throws JsonProcessingException  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1Buffer.toString());
            double  cpu_delta = jsonNode.at("/cpu_stats/cpu_usage/total_usage").asDouble()
                            - jsonNode.at("/precpu_stats/cpu_usage/total_usage").asDouble();
            
            double system_delta = jsonNode.at("/cpu_stats/system_cpu_usage").asDouble()
                                - jsonNode.at("/precpu_stats/system_cpu_usage").asDouble();
            
            double number_cpus = jsonNode.at("/cpu_stats/online_cpus").asDouble();
            if (system_delta==0) {
                System.out.println("\n\nSomething went wrong.\n"
                                 + "The formula to calculate CPU usage is:\n"
                                 + "(cpu_delta / system_delta) * number_cpus * 100.0\n"
                                 + "and system_delta is 0.\nMake sure that the container is running");
            }
            double cpuUsage = (cpu_delta / system_delta) * number_cpus * 100.0;
            return cpuUsage;

        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
        return 0.0;
    }

    
    /* Format the json response for container info to a user-friendly message
     *  NullPointerException if an error occurs while executing the http request
     * this Exception might occur when the image name is not found in the json file
     */ 
    public void printFormattedJson() throws JsonProcessingException  {
         try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            System.out.println("Container Name: " + jsonNode.get("Name").asText());
            System.out.println("Container ID: " + jsonNode.get("Id").asText());
            System.out.println("Status: " + jsonNode.get("State").get("Status").asText());
            System.out.println("Image ID: " + jsonNode.get("Image").asText());
            System.out.println("Network ID: " + jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("NetworkID").asText()); 
            System.out.println("Gateway: " + jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("Gateway").asText()); 
            System.out.println("IP Address: " + jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("IPAddress").asText()); 
            System.out.println("Mac Address: " + jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("MacAddress").asText()); 
            //System.out.println("Image Name: " + jsonNode.get("Config").get("Labels").get("org.opencontainers.image.title").asText());
        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
    }

    /** Return a String array with the prepared data that will be saved in a csv file 
     * @return str a String array with the data that will be saved in a csv file
     * @throws NullPointerException if an error occurs while executing the http request
    */
    public String[] prepareStorageData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            String[] str = new String[6];
            str[0] = jsonNode.at("/Name").asText(); //Container Name
            str[1] = jsonNode.at("/Id").asText(); //Container ID
            str[2] = jsonNode.at("/NetworkSettings/Networks/bridge/IPAddress").asText(); //IP Address
            str[3] = jsonNode.at("/NetworkSettings/Networks/bridge/MacAddress").asText(); //Mac Address
            this.getRequest = new HttpGet(ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/stats"  );
            executeHttpRequest("stats");
            str[4] = String.valueOf(lastCPUUsage); //CPU Usage
            //ZonedDateTime datetime = ZonedDateTime.now();
            //LocalDateTime datetime = LocalDateTime.now();
            LocalDate date = LocalDate.now(); 
            LocalTime time = LocalTime.now();
            str[5] = date.toString()+"  "+time.toString().substring(0,8); 
            return str;
          }catch (Exception e) {
            System.out.println("OOPSS SOMETHING WENT WRONG....");
            e.printStackTrace();
            return null;
         }
    }

    /**
     * Format the json response for image  -that you 've searched for- to a user-friendly message
     * @throws JsonProcessingException
     * @throws NullPointerException
     * @prints info about the top 3 images with the name that user searched. The info contains: 
     * 1.Image Name
     * 2.Description
     * 3.Star Count (the times an image has been shared)   
     */
    public void printFormattedJsonForImage() throws JsonProcessingException, NullPointerException {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response1.toString()); // could use .body()
                
                if (jsonNode.isArray()) {
                    System.out.println("TOP 3 SEARCHED RESULTS\n");
                    for (JsonNode el : jsonNode) {
                        System.out.println("Image name: " + el.get("name") 
                                            + "\nDescription: " 
                                            + el.get("description") 
                                            + "\nStar count: " 
                                            + el.get("star_count") +"\n" );
                    }
                }
            } catch (Exception e) {
                System.out.println("Oops, something went wrong...");
            }
    }
}