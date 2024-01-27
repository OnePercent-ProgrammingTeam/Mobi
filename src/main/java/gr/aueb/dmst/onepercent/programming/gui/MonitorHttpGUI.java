package gr.aueb.dmst.onepercent.programming.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;

/**
 * Class: MonitorHttpGUI extends MonitorHttp
 * This class provides HTTP-related functionality for the graphical user interface.
 */
public class MonitorHttpGUI extends MonitorHttp {

    String[] containerInfo = new String[4];

    /**
     * ok
     */
    public static int searchResultCount = 3;
    /** Method: inspectContainerGUI() retrieves information about a container that might be 
     * or might not be locally installed. 
     * This method does not show messages to command line. 
     */
    @Override
    public void inspectContainer() {
        try {
            String[] res = this.getTableforContainer();
            containerInfo[0] = res[6];
            containerInfo[1] = res[7];
            containerInfo[2] = res[4];
            containerInfo[3] = res[5];
        } catch (Exception e) {
            System.out.print("ERROR FROM inspectContainerGUI() method ");
        }
    }

    public String[] getContainerInfo() {
        return containerInfo;
    }

    @Override
    public String[] getTableforContainer() throws JsonProcessingException {
        String[] info = new String[8];
        try {
            System.out.println("Inside getTableforContainer() method");
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
   
    @Override
    public CloseableHttpResponse getContainerStats(String GOT_TO_DELETE) {
        String message = "stats"; // get the container statistics in json format
        conId = containerId;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);                        
        return getHttpResponse();
    }

    @Override
    /** Method: searchImagesGUI() searches for an image by it's name. The result is limited to 3. 
     * This method does not show messages to command line. */
    public void searchImage() {
        String message = "/images/search"; // get the container statistics in json format
        dataBaseThread.setImageName(imageName);
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=" + searchResultCount);
        System.out.println(MonitorHttp.DOCKER_HOST + message + "?term=" + 
            imageName + "&limit=10");
        executeHttpRequest(message);
    }

    /**
     * Method: getImagesListGUI()
     * Sends an HTTP request to retrieve a list of images with additional information.
     * The request includes parameters to exclude intermediate images and include shared image size.
     * Prints the request URL to the console.
     */
    public void getImagesListGUI() {
        String message = "/images/json";
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + message + 
                                "?boolean=false&shared-size=true");
        executeHttpRequest(message);
        System.out.println(MonitorHttp.DOCKER_HOST + message + 
                                "?boolean=false&shared-size=true");
    }


    /** Method: executeHttpRequest(String) executes the http request for 
     *  getting info about a container.
     * @param message the final part of the url that is used to get the info.
     * may throw Exception if an error occurs while executing the http request.
     */
    public void executeHttpRequest(String message) {
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(getRequest)) {
            
            System.out.println(getRequest);
            //CloseableHttpResponse response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            
            //int statusCode = response.getStatusLine().getStatusCode();
            //System.out.println("Status Code : " + statusCode);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            response1 = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (message.equals("stats")) {
                    lastCPUUsage = getCPUusage(response1); //print real time CPU Usage
                    response.close();
                    break;
                }
            }
            
            reader.close();
            response.close();
           
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + 
                                message + 
                                " the container: " + 
                                e.getMessage()); // Print the error message
        } 
    }


    /**
     * to do
     * @param string to do
     * @return to do
     */
    public StringBuilder getSearchResult(String string) {
        StringBuilder searchResult = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); // could use .body()
                
            if (jsonNode.isArray()) {
                for (JsonNode el : jsonNode) {
                    searchResult.append(el.get("name") + "\n" +
                        el.get("description") + "\n" + 
                        el.get("star_count") + "\n");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Oops, something went wrong...");
        }
        return searchResult;
    }



    

    /**
     * Method: getFormattedImageIdsList()
     * Parses the JSON response to extract formatted image IDs.
     *
     * @return An ArrayList containing formatted image IDs.
     *         If an error occurs during parsing, returns null.
     */
    public ArrayList<String> getFormattedImageIdsList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); 
            ArrayList<String> ids = new ArrayList<String>();
            if (jsonNode.isArray()) {
                for (JsonNode el : jsonNode) {
                    ids.add(el.get("Id").asText());
                }
            }
            return ids;
        } catch (Exception e) {
            System.out.println("Oops, something went wrong...");
            return null;
        }
    }

    /**
     * Method: getFormattedImageNamesList()
     * Parses the JSON response to extract formatted image names.
     *
     * @return An ArrayList containing formatted image names.
     *         If an error occurs during parsing, returns null.
     */
    public ArrayList<String> getFormattedImageNamesList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());     
            ArrayList<String> names = new ArrayList<String>();
            
            if (jsonNode.isArray()) {
                for (JsonNode jn : jsonNode) {
                    
                    if (jn.get("RepoTags").size() != 0) {
                        names.add(jn.get("RepoTags").get(0).asText());
                    }
                }
            }
            return names;
        } catch (JsonProcessingException e) {
            System.out.println("-------------------------------------");
            System.out.println("Exception in getFormattedImageNamesList() method");
            System.out.println(e.getClass());
            System.out.println("-------------------------------------");
            return null;
        }
    }

    public String[] getSwarmInfo() {
        String[] swarmInfo = new String[6];
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            
            swarmInfo[0] = jsonNode.get("Spec").get("Name").asText();
            swarmInfo[1] = jsonNode.get("ID").asText();
            swarmInfo[2] = jsonNode.get("Version").get("Index").asText();
            swarmInfo[3] = jsonNode.get("CreatedAt")
                                    .asText()
                                    .replace("T", "\n")
                                    .substring(0, 19);
            swarmInfo[4] =  jsonNode
                            .get("UpdatedAt")
                            .asText()
                            .replace("T", "\n")
                            .substring(0, 19);
            swarmInfo[5] =  jsonNode.get("SubnetSize").asText();
            return swarmInfo;
        } catch (Exception e) {
            System.out.println("Some information is missing...");
        }
        return null;
    }

    /**
     * Default Constructor
     */
    public MonitorHttpGUI() {

    }
}
