package gr.aueb.dmst.onepercent.programming;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import io.netty.handler.codec.http.HttpResponse;

public class ContainerManagerHttp {
    
    protected static final String DOCKER_HOST = "http://localhost:2375"; //  docker host address 
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault(); // Create an HTTP client
    private static HttpPost postRequest; // Create an HTTP POST request
    protected static String containerId;
    private static HttpEntity entity;
   

    public static void startContainer() {
        String message = "start";
        containerId = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpPostRequest(message);
    }

    public static void stopContainer() {
        String message = "stop";
        containerId = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpPostRequest(message);
    }

    public static void pullImage() {
        String message = "pull";
        String imageName = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeHttpPostRequest(message);
    }
    
    public static void executeHttpPostRequest(String message) {
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(postRequest);
             // Start the container
             entity = response.getEntity(); // Get the response entity
            System.out.println("Container " + message  + " was successfull.");
            if (entity != null) { // If the response entity is not null, print it on the console
               handleResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } finally {
            EntityUtils.consumeQuietly(postRequest.getEntity()); // Release the resources of the request
        }
    }
    
    public static void handleResponse() throws IOException{
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) { // Create a reader for the response content
                    String line;
                    while ((line = reader.readLine()) != null) { // Print the response line by line
                        System.out.println(line);
                     }
        }
    }
   
    
   



}

