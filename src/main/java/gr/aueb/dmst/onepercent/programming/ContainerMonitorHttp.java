package gr.aueb.dmst.onepercent.programming;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;

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
            }
            
            reader.close();
            if (message.equals("json")){
                printFormattedJson();
                prepareStorageData(); 
            }else if (message.equals("/images/search"))
                System.out.println(response1.toString());
                printFormattedJsonForImage();
               
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
     * @param response1Buffer a StringBuffer object
     * @throws NullPointerException if an error occurs while executing the http request
     */
    public double getFormattedStats(StringBuffer response1Buffer) throws JsonProcessingException  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1Buffer.toString());
            //System.out.println(jsonNode.get("memory_stats").get("usage").asDouble());
            double cpu_delta = jsonNode.get("cpu_stats").get("cpu_usage").get("total_usage").asDouble()
                            - jsonNode.get("precpu_stats").get("cpu_usage").get("total_usage").asDouble();
            double system_delta = jsonNode.get("cpu_stats").get("system_cpu_usage").asDouble()
                            - jsonNode.get("precpu_stats").get("system_cpu_usage").asDouble();
            double number_cpus = jsonNode.get("cpu_stats").get("online_cpus").asDouble();
            
            
            return (system_delta==0.0)? (cpu_delta / system_delta) * number_cpus * 100.0   : 0.0;

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
            String[] str = new String[5];
            str[0] = jsonNode.get("Name").asText(); //Container Name
            str[1] = jsonNode.get("Id").asText(); //Container ID
            str[2] = jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("IPAddress").asText(); //IP Address
            str[3] = jsonNode.get("NetworkSettings").get("Networks").get("bridge").get("MacAddress").asText(); //Mac Address
            double res = getFormattedStats(ContainerMonitorHttp.response1);
            String result = Double.toString(res);
            str [4] = result; //CPU Usage
            return str;
          }catch (Exception e) {
            System.out.println("OOPSS SOMETHING WENT WRONG....");
            return null;
         }
    }

    /**
     * Format the json response for image  -that you 've searched for- to a user-friendly message
     * @throws JsonProcessingException
     * @throws NullPointerException
     * We have to fix the method because it doesn't work properly
     */
    public void printFormattedJsonForImage() throws JsonProcessingException, NullPointerException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response1.toString());
        System.out.println("Image Name:" + jsonNode.get("name").asText());
        System.out.println("Image Description:" + jsonNode.get("description").asText());
    }
}