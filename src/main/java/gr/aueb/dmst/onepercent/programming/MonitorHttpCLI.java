package gr.aueb.dmst.onepercent.programming;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MonitorHttpCLI extends MonitorHttp {
     /** Method: inspectContainer() retrieves information about a container that might be 
     * or might not be locally installed.*/
    @Override
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

    /** Method: getContainerStats(String) gets statistics about a running container 
     *  (especially CPU usage). 
     *  @param calledby the name of the class that called this method.
     *  These stats are used in Graph class in order to create a graph.
     *  @see Graph
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

    /** Method: searchImages() searches for an image by it's name. The result is limited to 3. */
    @Override
    public void searchImage() {
        String message = "/images/search"; // get the container statistics in json format
        imageName = Main.handleInput("Please type the name of the image you want to search for: ");
        imName = imageName;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=3");
        executeHttpRequest(message);
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



    /** Method: executeHttpRequest(String) executes the http request for 
     *  getting info about a container.
     * @param message the final part of the url that is used to get the info.
     * @throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try { 
            this.response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            //int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("Status Code : " + statusCode);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.response.getEntity().getContent()));
            String inputLine;
            MonitorHttp.response1 = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (message.equals("stats")) {
                    lastCPUUsage = getFormattedStats(response1); //print real time CPU Usage
                    this.response.close();
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
