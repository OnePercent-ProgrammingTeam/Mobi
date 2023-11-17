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
    
    /** Docker host address*/ 
    protected static final String DOCKER_HOST = "http://localhost:2375"; 
    /** Create an http client */
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault(); 
    /** Http Post request (Post is "do to something e.g start container") */
    private static HttpPost postRequest; 
    protected static String containerId;
    private static HttpEntity entity;
   
    /** Start container with http request */
    public static void startContainer() {
        String message = "start";
        containerId = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpPostRequest(message);
    }

    /** Stop container with http request */
    public static void stopContainer() {
        String message = "stop";
        containerId = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpPostRequest(message);
    }

    /** Pull an image with http request, 
     *  the image name is given by the user,
     *  practically is like starting a container 
     * with an image that does not exist locally
    */
    public static void pullImage() {
        String message = "pull";
        String imageName = Test.handleInput(message);
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeHttpPostRequest(message);
    }
    
    /** Execute the http request (start/stop container & pull image)
     * @param message the message that is given by the user
     * @throws Exception if an error occurs while executing the http request
     */
    public static void executeHttpPostRequest(String message) {
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(postRequest);
             // Start the container
             entity = response.getEntity();
            System.out.println("Container " + message  + " was successfull.");
            if (entity != null) { // If the response entity is not null, print it to the console
               handleResponse();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace of the error
            System.err.println("Failed to " + message + " the container: " + e.getMessage()); // Print the error message
        } finally {
            EntityUtils.consumeQuietly(postRequest.getEntity()); // Release the resources of the request
        }
    }
    
    /**
     * Reads and prints the content of an input stream line by line.
    *
    * This method takes an input stream, wraps it with a BufferedReader, and reads its content
    * line by line. Each line is then printed to the console. The BufferedReader is automatically
    * closed (It is autoclosable. That is why we use the parenthesis in try segment of try-catch statement.
    * It would be the same, if we had a finally statement, in which we would "close" with .close() method the 
    * BufferedReader object) upon exiting the method, due to the try-with-resources statement.
    *
    * @param entity The HTTP response entity containing the input stream to be read.
    * @throws IOException If an I/O error occurs while reading the input stream.
    */

    public static void handleResponse() throws IOException{
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) { // Create a reader for the response content
                    String line;
                    while ((line = reader.readLine()) != null) { // Print the response line by line
                        System.out.println(line);
                     }
        }
    }
   
    
   



}

