package gr.aueb.dmst.onepercent.programming;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import exceptions.StartContainerException;
import exceptions.StopContainerException;
import exceptions.PullImageException;
import com.google.common.annotations.VisibleForTesting;

/**
 * ManagerHttpCLI class extends ManagerHttp and provides an implementation 
 * of managing Docker containers and images
 * using HTTP requests. It includes methods to start, stop containers, and pull images.
 * Regular ANSI color codes are defined as constants for colorful console output.
 */
public class ManagerHttpCLI extends ManagerHttp {
    
    /**
     * ANSI color code for resetting text color.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    
    /**
     * ANSI color code for green text.
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * ANSI color code for yellow text.
     */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /** Method: startContainer() starts container with http request. */
    @Override
    public void startContainer() {
        String message = "start";
        containerId = Main.handleInput("Please type the container ID to start the container: ");
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpRequest(message);
    }

    /** Method: stopContainer() stops container with http request. */
    @Override
    public void stopContainer() {
        String message = "stop";
        containerId = Main.handleInput("Please type the container ID to stop the container: ");
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpRequest(message);
    }
    
    /** Method: pullImage() pulls an image with http request, the image name is given by the user,
     *  practically is like starting a container with an image that does not exist locally.
     */
    @Override
    public void pullImage() {
        String message = "pull";
        String imageName;
        imageName = Main.handleInput("Please type the name of the image you would like to pull:");
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeHttpRequest(message);
    }

    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            this.response = HTTP_CLIENT.execute(postRequest); // Start the container
            entity = this.response.getEntity();     
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

    /** Method handleOutput(String) prints the appropriate message, based on the status 
     *  code of the http response and the request that has been done.
     * @param message the message that indicates the action that is going to be executed. 
     * @return the correct message
     */
    public String handleOutput(String message) {
        String output = "";
        if (this.response == null) {
            output = "response has not been initialized";
        } else {
            try {
                output = provideMessage(message);
            } catch (StartContainerException e) {
                output = e.getMessage();
            } catch (StopContainerException e) {
                output = e.getMessage();
            } catch (PullImageException e) {
                output = e.getMessage();
            }
        
        }
        System.out.println(output);
        return output;

    }

    /**
     * Retrieves the message provided by {@code provideMessage(String)} method.
     *
     * This method is intended for testing purposes to access the private
     * {@code provideMessage} method, which generates output based on the HTTP
     * response status code and the given message.
     *
     * @param message The message that indicates the action that is going to be executed.
     * @return The generated output message 
     * based on the HTTP response status code and the input message.
     * @see #provideMessage(String)
     */
    @VisibleForTesting
    public String getProvideMessage(String message) {
        return provideMessage(message);
    }
    private String provideMessage(String message) {
        String output = "";
        if (message.equals("start")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 204:
                    output = ANSI_GREEN + "Container " + message  + " was successfull." +
                        ANSI_RESET;
                    break;
                case 304:
                    throw new StartContainerException("Container already started.");
                case 404:
                    throw new StartContainerException("There is no such container. Try again");
                case 500:
                    throw new StartContainerException("Server error!");
            }
        } else if (message.equals("stop")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 204:
                    output = ANSI_GREEN + "Container " + message  + " was successfull." + 
                        ANSI_RESET;
                    break;
                case 304:
                    throw new StopContainerException("Container already stopped.");
                case 404:
                    throw new StopContainerException("There is no such container. Try again");
                case 500:
                    throw new StopContainerException("Server error!");
            }
        } else if (message.equals("pull")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 200:
                    output = ANSI_GREEN + "Image " + message + " was successfull." + ANSI_RESET;
                    break;
                case 404:
                    throw new PullImageException("Image not found.");
                case 500:
                    throw new PullImageException("Server error!");
            }
        }
        return output;
    }

    /**
     * Default Constructor
     */
    public ManagerHttpCLI() {

    }
}
