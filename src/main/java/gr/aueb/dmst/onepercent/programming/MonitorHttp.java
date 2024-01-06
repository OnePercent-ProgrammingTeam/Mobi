package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStreamReader;
import java.io.BufferedReader;
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
public class MonitorHttp extends SuperHttp {
    
    private static String[] containerInfoForGUI = {"Not Found", "Not Found", "Not Found",
        "Not Found", "Not Found", "Not Found", "Not Found", "Not Found"};

    /** Method: inspectContainer() retrieves information about a container that might be 
     * or might not be locally installed.*/
    public void inspectContainer() {
        String message = "json"; // get the container information in json format
        MonitorHttp.containerId = Main.handleInput(
            "Please type the container ID to get info about the container: ");
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);
        executeHttpRequest(message);
    }

    /** Method: inspectContainerGUI() retrieves information about a container that might be 
     * or might not be locally installed. 
     * This method does not show messages to command line. 
     */
    public void inspectContainerGUI() {
        try {
            containerInfoForGUI = new String[8];
            containerInfoForGUI = getTableforContainer();
        } catch (Exception e) {
            System.out.print("ERROR FROM inspectContainerGUI() method ");
            for (int i = 0; i < containerInfoForGUI.length; i++) {
                containerInfoForGUI[i] = "Not Found";
            }
        }
    }

    public String[] getContainerInfoForGUI() {
        return containerInfoForGUI;
    }

    /**The static field "conId" is used to keep the id of the container the user wants to find,  
     * in order to be visible in the database. 
     */
    public static String conId;

    /** Method: getContainerStats(String) gets statistics about a running container 
     *  (especially CPU usage). 
     *  @param calledby the name of the class that called this method.
     *  These stats are used in Graph class in order to create a graph.
     *  @see Graph
    */
    public CloseableHttpResponse getContainerStats(String calledby) {
        String message = "stats"; // get the container statistics in json format
        String outputMessage;
        if (calledby.equals("Graph")) {
            outputMessage = "Please type the container ID to plot diagram with CPU usage: ";
        } else {
            outputMessage = "Please type the ID of the running container to save real time data: ";
        }
        MonitorHttp.containerId = Main.handleInput(outputMessage);
        conId = containerId;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);                        
        return getHttpResponse(message);
    }

    public CloseableHttpResponse getContainerStatsGUIforGraph() {
        String message = "stats"; // get the container statistics in json format
        conId = containerId;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);                        
        return getHttpResponse(message);
    }

    /**The static field "imName" is used to keep the name of the image the user wants to find,  
     * in order to be visible in the database. 
     */
    public static String imName;

    /** Method: searchImages() searches for an image by it's name. The result is limited to 3. */
    public void searchImages() {
        String message = "/images/search"; // get the container statistics in json format
        imageName = Main.handleInput("Please type the name of the image you want to search for: ");
        imName = imageName;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=3");
        executeHttpRequest(message);
    }

    /** Method: searchImagesGUI() searches for an image by it's name. The result is limited to 3. 
     * This method does not show messages to command line. */
    public void searchImagesGUI() {
        String message = "/images/search"; // get the container statistics in json format
        imName = imageName;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=3");
        executeHttpRequest(message);
    }

    

   

    /** Method: executeHttpRequest(String) executes the http request for 
     *  getting info about a container.
     * @param message the final part of the url that is used to get the info.
     * @throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            CloseableHttpResponse response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            //int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("Status Code : " + statusCode);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            MonitorHttp.response1 = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (message.equals("stats")) {
                    lastCPUUsage = getFormattedStats(response1); //print real time CPU Usage
                    response.close();
                    break;
                }
            }
            
            reader.close();
            
            if (message.equals("json")) {
                printFormattedInfo();
                
            }
            if (message.equals("/images/search")) {
                printFormattedJsonForImage();
            } 
                               
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + 
                                message + 
                                " the container: " + 
                                e.getMessage()); // Print the error message
        } 
    }

    /** Method: getHttpResponse(String) executes the http request for getting 
     *  stats about a running container.
     * @param message the final part of the url that is used to get the info.
     * @throws Exception if an error occurs while executing the http request.
     */
    @Override
    public CloseableHttpResponse getHttpResponse(String message) {
        try {
            CloseableHttpResponse response = MonitorHttp.HTTP_CLIENT.execute(getRequest);

            //int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("Status Code : " + statusCode);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + 
                                message + 
                                " the container: " + 
                                e.getMessage()); // Print the error message
        } 
        return null;
    }

    /** Method: getFormattedStats(StringBuffer) formats the json response for stats 
     *  to a user-friendly message.
     *  that is real-time updated and printed to the console
     *  @param response1Buffer a StringBuffer object.
     *  @throws NullPointerException if an error occurs while executing the http request.
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
     */
    
    public double getFormattedStats(StringBuffer response1Buffer) throws JsonProcessingException {
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

    
    /** Method: printFormattedInfo() reads the json response for container info 
     *  to a user-friendly message.
     * NullPointerException if an error occurs while executing the http request.
     * this Exception might occur when the image name is not found in the json file.
     */ 
    public void printFormattedInfo() throws JsonProcessingException  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            System.out.println("Container Name: " + jsonNode.get("Name").asText().substring(1));
            System.out.println("Container ID: " + jsonNode.get("Id").asText());
            System.out.println("Status: " + jsonNode.get("State").get("Status").asText());
            System.out.println("Image ID: " + jsonNode.get("Image").asText());
            System.out.println("Network ID: " + jsonNode
                                                .get("NetworkSettings")
                                                .get("Networks")
                                                .get("bridge")
                                                .get("NetworkID")
                                                .asText()); 
            System.out.println("Gateway: " + jsonNode
                                             .get("NetworkSettings")
                                             .get("Networks")
                                             .get("bridge")
                                             .get("Gateway")
                                             .asText()); 
            System.out.println("IP Address: " + jsonNode
                                                .get("NetworkSettings")
                                                .get("Networks")
                                                .get("bridge")
                                                .get("IPAddress")
                                                .asText()); 
            System.out.println("Mac Address: " + jsonNode
                                                 .get("NetworkSettings")
                                                 .get("Networks")
                                                 .get("bridge")
                                                 .get("MacAddress")
                                                 .asText()); 
            /*System.out.println("Image Name: " + jsonNode
                                                  .get("Config")
                                                  .get("Labels")
                                                  .get("org.opencontainers.image.title")
                                                  .asText());*/
        } catch (NullPointerException e) {
            System.out.println("Exception due to null value");
        }
    }

    

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

    /** Method: printFormattedJsonForImage() formats the json response for image  
     * -that you 've searched for- to a user-friendly message.
     *  @throws JsonProcessingException
     *  @throws NullPointerException
     *  @prints info about the top 3 images with the name that user searched. The info contains: 
     *  1.Image Name
     *  2.Description
     *  3.Star Count (the times an image has been shared)   
     */
    public void printFormattedJsonForImage() throws JsonProcessingException, NullPointerException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); // could use .body()
                
            if (jsonNode.isArray()) {
                System.out.println("Top 3 searched results\n");
                for (JsonNode el : jsonNode) {
                    System.out.println("Image name: " + el.get("name") 
                        + "\nDescription: " 
                        + el.get("description") 
                        + "\nStar count: " 
                        + el.get("star_count") + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Oops, something went wrong...");
        }
    }
}
