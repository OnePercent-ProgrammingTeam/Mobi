import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper; // Import the Jackson ObjectMapper class for formatting the JSON response
import com.fasterxml.jackson.databind.JsonNode; // Import the Jackson JsonNode class for formatting the JSON response
import com.fasterxml.jackson.core.JsonProcessingException; // Import the Jackson JsonProcessingException 

public class ContainerMonitorHttp {
    private static HttpGet getRequest;
    private static String imageName; 

    public static void getContainerInformation() {
        String message = "json"; // get the container statistics in json format
        ContainerManagerHttp.containerId = Test.handleInput(message);
        getRequest = new HttpGet(ContainerManagerHttp.dockerHost + "/containers/" + ContainerManagerHttp.containerId + "/" + message );
        System.out.println("Follow the link for " + message +" info:\n" + "LINK: " + ContainerManagerHttp.dockerHost + "/containers/" + ContainerManagerHttp.containerId + "/" + message + "\n\n");
        executeHttpGetRequest(message);
    }

    public static CloseableHttpResponse getContainerStats() {
        String message = "stats"; // get the container statistics in json format
        ContainerManagerHttp.containerId = Test.handleInput(message);
        getRequest = new HttpGet(ContainerManagerHttp.dockerHost + "/containers/" + ContainerManagerHttp.containerId + "/" + message );
        System.out.println("Follow the link for " + message +" info:\n" + "LINK: " + ContainerManagerHttp.dockerHost + "/containers/" + ContainerManagerHttp.containerId + "/" + message + "\n\n");
        return executeHttpGetRequestForStats(message);
    }

    public static void executeHttpGetRequest(String message) {
        try {
            CloseableHttpResponse response = ContainerManagerHttp.httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code : " + statusCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            StringBuffer response1 = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
            }
            //
            reader.close();
            if (message.equals("json"))
                printFormattedJson(response1);
            else if (message.equals("/images/search"))
                System.out.println(response1.toString());
                printFormattedJsonForImage(response1);
            
            
        
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } 
    }

    public static CloseableHttpResponse executeHttpGetRequestForStats(String message) {
        try {
            CloseableHttpResponse response = ContainerManagerHttp.httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code : " + statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } 
        return null;
    }


    public static double getFormattedStats(StringBuffer response1) throws JsonProcessingException  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString());
            //System.out.println(jsonNode.get("memory_stats").get("usage").asDouble());
            return jsonNode.get("memory_stats").get("usage").asDouble();
        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
        return 0.0;
    }


    public static void printFormattedJson(StringBuffer response1) throws JsonProcessingException  {
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
            System.out.println("Image Name: " + jsonNode.get("Config").get("Labels").get("org.opencontainers.image.title").asText());
        } catch (NullPointerException e) {
            System.out.println("------------------------------------------");
        }
    }

    public static void printFormattedJsonForImage(StringBuffer response1) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response1.toString());
        System.out.println("Image Name:" + jsonNode.get("name").asText());
        System.out.println("Image Description:" + jsonNode.get("description").asText());
    }


    public static void searchImages() {
        
        String message = "/images/search"; // get the container statistics in json format
        imageName = Test.handleInput("Please type the name of the image you want to search for: ");
        getRequest = new HttpGet(ContainerManagerHttp.dockerHost + message + "?term="+ imageName + "&limit=3" );
        System.out.println(ContainerManagerHttp.dockerHost + message + "?term="+ imageName + "&limit=3");
        executeHttpGetRequest(message);
    }

    /** Στην εκτέλεση της τελευταίας εντολής, όπου εμφανίζεται το Image Name είναι πιθανό σε συγκεκριμένα containers να προκύψει
     * NullPointerException. Αυτό οφείλεται στο ότι ενδέχεται στο json file να μην εμφανίζεται το Image Name. Σε αυτή την περίπτωση
     * εμφανίζεται το μήνυμα "------------------------------------------" και συνεχίζεται η εκτέλεση του προγράμματος λόγω της
     * χρήσης της try-catch. Σε όλες τις άλλες περιπτώσεις εμφανίζονται κανονικά όλα τα στοιχεία του container.
     */
}