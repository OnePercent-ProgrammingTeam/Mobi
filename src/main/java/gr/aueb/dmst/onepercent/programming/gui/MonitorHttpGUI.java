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

    private static String[] containerInfoForGUI = {"Not Found", "Not Found", "Not Found",
        "Not Found", "Not Found", "Not Found", "Not Found", "Not Found"};


    /** Method: inspectContainerGUI() retrieves information about a container that might be 
     * or might not be locally installed. 
     * This method does not show messages to command line. 
     */
    @Override
    public void inspectContainer() {
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

    /**
     * Method: getContainerInfoForGUI()
     * Retrieves the container information formatted for the graphical user interface.
     *
     * @return An array containing container information for the GUI, including:
     *         - Container ID
     *         - Container Name
     *         - Container Status
     *         - Image ID
     *         - Network ID
     *         - Gateway
     *         - IP Address
     *         - Mac Address
     */
    public String[] getContainerInfoForGUI() {
        return containerInfoForGUI;
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
        imName = imageName;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=3");
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
     */
    public StringBuilder getSearchResult(String string) {
        StringBuilder searchResult = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); // could use .body()
                
            if (jsonNode.isArray()) {
                for (JsonNode el : jsonNode) {
                    searchResult.append("Image name: " + el.get("name") 
                        + "\nDescription: " 
                        + el.get("description") 
                        + "\nStar count: " 
                        + el.get("star_count") + "\n");
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

    /**
     * Default Constructor
     */
    public MonitorHttpGUI() {

    }
}
