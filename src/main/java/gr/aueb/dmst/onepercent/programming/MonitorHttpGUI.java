package gr.aueb.dmst.onepercent.programming;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public ArrayList<String> getFormattedImageNamesList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); 
            ArrayList<String> names = new ArrayList<String>();
            if (jsonNode.isArray()) {
                for (JsonNode jn : jsonNode) {
                    
                    
                    names.add(jn.get("RepoTags").get(0).asText());
                }
            }
            return names;
        } catch (Exception e) {
            System.out.println("Oops, something went wrong...");
            return null;
        }
    }


}