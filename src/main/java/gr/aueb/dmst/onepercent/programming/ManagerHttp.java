package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Class: ManagerHttp is responsible for the http requests that are made to the docker daemon
 *  in order to start/stop a container or pull an image.
 *  It extends the SuperHttp class.
 *  @see SuperHttp
 */
public class ManagerHttp extends SuperHttp {
    
    private static HttpEntity entity;
   
    /** Method: startContainer() starts container with http request. */
    public void startContainer() {
        String message = "start";
        containerId = Main.handleInput("Please type the container ID to start the container: ");
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpRequest(message);
    }

    /** Method: stopContainer() stops container with http request. */
    public void stopContainer() {
        String message = "stop";
        containerId = Main.handleInput("Please type the container ID to stop the container: ");
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpRequest(message);
    }

    /** Method: pullImage() pulls an image with http request, the image name is given by the user,
     *  practically is like starting a container with an image that does not exist locally.
     */
    public void pullImage() {
        String message = "pull";
        String imageName;
        imageName = Main.handleInput("Please type the name of the image you would like to pull:");
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeHttpRequest(message);
    }
    
    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * @throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            this.response = HTTP_CLIENT.execute(postRequest); // Start the container
            entity = response.getEntity();
            handleOutput(message);
            
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace of the error
            String object = null;
            object = (message.equals("start") || message.equals("stop")) ?  "container" :  "image";
            System.err.println("Failed to " + 
                                message + 
                                " the" + 
                                object + 
                                e.getMessage()); // Print the error message
        } finally {
            // Release the resources of the request
            EntityUtils.consumeQuietly(postRequest.getEntity()); 
        }
    }
    
    /**
    * Method: handleResponse() reads and prints the content of an input stream line by line.
    *
    * This method takes an input stream, wraps it with a BufferedReader, and reads its content
    * line by line. Each line is then printed to the console. The BufferedReader is automatically
    * closed (It is autoclosable. That is why we use the parenthesis in try segment of try-catch
    * statement. It would be the same, if we had a finally statement, in which we would "close"
    * with .close() method the 
    * BufferedReader object) upon exiting the method, due to the try-with-resources statement.
    *
    * @param entity The HTTP response entity containing the input stream to be read.
    * @throws IOException If an I/O error occurs while reading the input stream.
    */
    public void handleResponse() throws IOException {
        // Create a reader for the response content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity
                                                                              .getContent()))) { 
            String line;
            while ((line = reader.readLine()) != null) { // Print the response line by line
                System.out.println(line);
            }
        }
    }

    /** Method handleOutput(String) prints the appropriate message, based on the status 
     *  code of the http response and the request that has been done.
     * @param message the message that indicates the action that is going to be executed. 
     */
    public String handleOutput(String message) {
        String output = "";
        if (response == null) {
            output = "response has not been initialized";
        } else {
            output = provideMessage(message);
        }
        
      
        System.out.println(output);
        return output;

    }

    private String provideMessage(String message) {
        String output = "";
        if (message.equals("start")) {
            switch (response.getStatusLine().getStatusCode()) {
                case 204:
                    output = "Container " + message  + " was successfull.";
                    break;
                case 304:
                    output = "Container already started.";
                    break;
                case 404:
                    output = "There is no such container. Try again";
                    break;
                case 500:
                    output = "Server error!";
            }
        } else if (message.equals("stop")) {
            switch (response.getStatusLine().getStatusCode()) {
                case 204:
                    output = "Container " + message  + " was successfull.";
                    break;
                case 304:
                    output = "Container already stopped.";
                    break;
                case 404:
                    output = "There is no such container. Try again";
                    break;
                case 500:
                    output = "Server error!";
            }
        } else if (message.equals("pull")) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    output = "Image " + message + " was successfull.";
                    break;
                case 404:
                    output = "Image not found.";
                    break;
                case 500:
                    output = "Server error!";
            }
        }
        return output;
    }
}
