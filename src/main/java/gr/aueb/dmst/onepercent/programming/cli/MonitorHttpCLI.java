package gr.aueb.dmst.onepercent.programming.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

/**
 * Class: MonitorHttpCLI extends MonitorHttp
 * 
 * Description: Provides CLI-specific implementation 
 * for monitoring Docker containers through HTTP requests.
 * @see gr.aueb.dmst.onepercent.programming.core.MonitorHttp
 * @see gr.aueb.dmst.onepercent.programming.core.Graph
 */
public class MonitorHttpCLI extends MonitorHttp {
     /** Method: inspectContainer() retrieves information about a container that might be 
     * or might not be locally installed.*/
    @Override
    public void inspectContainer() {
        String message = "json"; // get the container information in json format
        MonitorHttp.containerId = Main.handleInput(
            "Please type the container ID to get info about the container: ");
        conId = containerId;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);
        executeHttpRequest(message);
    }

    /** Method: searchImages() searches for an image by it's name. The result is limited to 3. */
    @Override
    public void searchImage() {
        String message = "/images/search"; // get the container statistics in json format
        imageName = Main.handleInput("Please type the name of the image you want to search for: ");
        dataBaseThread.setImageName(imageName);
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=3");
        executeHttpRequest(message);
    }


    public StringBuilder formatSwarmInfo() {
        StringBuilder swarmInfo = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            
            swarmInfo.append("\nSwarm Name: " + jsonNode.get("Spec").get("Name").asText() + "\n");
            swarmInfo.append("Swarm ID: " + jsonNode.get("ID").asText() + "\n");
            swarmInfo.append("Version: " + jsonNode.get("Version").get("Index").asText() + "\n");
            swarmInfo.append("Created at: " + jsonNode
                                                    .get("CreatedAt")
                                                    .asText()
                                                    .replace("T", " ")
                                                    .substring(0, 19) + "\n");
            swarmInfo.append("Updated at: " + jsonNode
                                                    .get("UpdatedAt")
                                                    .asText()
                                                    .replace("T", " ")
                                                    .substring(0, 19) + "\n");
            swarmInfo.append("Subnetwork size: " + jsonNode
                                                    .get("SubnetSize")
                                                    .asText() + "\n");
            if (!(jsonNode.get("JoinTokens").get("Worker").isNull())) {
                swarmInfo.append("There is at least one worker in the swarm\n");
            } 
            if (!(jsonNode.get("JoinTokens").get("Manager").isNull())) {
                swarmInfo.append("There is at least one manager in the swarm\n");
            }
            return swarmInfo;
        } catch (Exception e) {
            System.out.println("Some information is missing...");
        }
        return swarmInfo.append(" ");
    }

    /** Method: getContainerStats(String) gets statistics about a running container 
     *  (especially CPU usage). 
     *  @param calledby the name of the class that called this method.
     *  These stats are used in Graph class in order to create a graph.
    */
    @Override
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
        return getHttpResponse();
    }

    

    /**
     * Method: printFormattedInfo() reads the JSON response for container info 
     * to a user-friendly message.
     * Throws NullPointerException if an error occurs while executing the HTTP request.
     * This exception might occur when the image name is not found in the JSON file.
     * 
     * @throws JsonProcessingException if there is an error processing the JSON response.
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



    /** Method: executeHttpRequest(String) executes the http request for 
     *  getting info about a container.
     * @param message the final part of the url that is used to get the info.
     * may throw Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try { 
            this.response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.response.getEntity().getContent()));
            String inputLine;
            MonitorHttp.response1 = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (message.equals("stats")) {
                    lastCPUUsage = getCPUusage(response1); //print real time CPU Usage
                    //dataBaseThread.setState("success");
                    //print real time Memory Usage
                    lastMemoryUsage = getMemoryUsage(response1); 
                    this.response.close();
                    break;
                }
            }
            
            reader.close();
            
            if (message.equals("json")) {
                //check other situations with the state
                dataBaseThread.setState("success");
                printFormattedInfo();
                
            }
            if (message.equals("/images/search")) {
                //check other situations with the state
                dataBaseThread.setState("success");
                printFormattedJsonForImage();
            } 

            if (message.equals("/images/json")) {
                System.out.println("Inside executeHttpRequest() method");
            }
                               
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + 
                                message + 
                                " the container: " + 
                                e.getMessage()); // Print the error message
        } 
    }

    /**
     * Method: printFormattedJsonForImage() formats the JSON response for an image 
     * -that you've searched for- to a user-friendly message.
     * 
     * @throws JsonProcessingException if there is an error processing the JSON response.
     * @throws NullPointerException if there is a null value in the JSON response.
     * prints info about the top 3 images with the name that the user searched. The info contains:
     *   1. Image Name
     *   2. Description
     *   3. Star Count (the times an image has been shared)
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

    /**
     * Default Constructor
     */
    public MonitorHttpCLI() {

    }
}

