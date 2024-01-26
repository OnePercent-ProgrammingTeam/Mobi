package gr.aueb.dmst.onepercent.programming.cli;

import com.google.common.annotations.VisibleForTesting;

import exceptions.PullImageException;
import exceptions.RemoveDockerObjectException;
import exceptions.ActionContainerException;

import gr.aueb.dmst.onepercent.programming.core.ManagerHttp;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

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
    
    @Override
    public void removeContainer() {
        String message = "remove";
        containerId = Main.handleInput("Please type the container ID to remove the container: ");
        deleteRequest = new HttpDelete(DOCKER_HOST + 
                                      "/containers/" + 
                                      containerId + "?force=true");
        
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

    @Override 
    public void removeImage() {
        String message = "removeImg";
        String imageName;
        imageName = Main.handleInput("Please type the name of the image you would like to remove:");
        deleteRequest = new HttpDelete(DOCKER_HOST + "/images/" + imageName);
        executeHttpRequest(message);
    }


    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            switch (message) {
                case "start":
                case "stop":
                case "pull":
                    this.response = HTTP_CLIENT.execute(postRequest); // Start the container
                    break;
                case "remove":
                case "removeImg":
                    this.response = HTTP_CLIENT.execute(deleteRequest); // Start the container
                    break;
            }
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
            if (!(message.equals("remove") || message.equals("removeImg")))
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
            } catch (ActionContainerException e) {
                output = e.getMessage();
            } catch (PullImageException e) {
                output = e.getMessage();
            } catch (RemoveDockerObjectException e) {
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
     * @throws ActionContainerException if the container is already started, not found, 
     * or server error.
     * @throws StopContainerException if the container is already stopped, not found,
     * or server error.
     * @throws PullImageException if the image is not found or server error.
     * @throws RemoveContainerException if the container is not found, bad parameter,
     * conflict, or server error.
     */
    @VisibleForTesting
    public String getProvideMessage(String message) {
        return provideMessage(message);
    }
    private String provideMessage(String message) throws ActionContainerException,
                                                        PullImageException,
                                                        RemoveDockerObjectException {
        String output = "";
        if (message.equals("start") || message.equals("stop")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 204:
                    conId = containerId; //only if it successful insert it into database
                    output = ANSI_GREEN + "Container " + message  + " was successfull." +
                        ANSI_RESET;
                    break;
                case 304:
                    message = (message.equals("start")) ? "start" : "stopp";
                    throw new ActionContainerException("Container already " + message + "ed.");
                case 404:
                    throw new ActionContainerException("There is no such container. Try again");
                case 500:
                    throw new ActionContainerException("Server error!");
            }
        } else if (message.equals("pull")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 200:
                    imName = imageName;
                    output = ANSI_GREEN + "Image " + message + " was successfull." + ANSI_RESET;
                    break;
                case 404:
                    throw new PullImageException("Image not found.");
                case 500:
                    throw new PullImageException("Server error!");
            }
        } else if (message.equals("remove") || message.equals("removeImg")) {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 204:
                    if (message.equals("remove")) {
                        conId = containerId;
                    } else {
                        imName = imageName;
                    }
                    output = ANSI_GREEN + "Successfull removal." + ANSI_RESET;
                    break;
                case 400:
                    throw new RemoveDockerObjectException("Bad parameter");
                case 404:
                    throw new RemoveDockerObjectException("No such object found. Try again");
                case 409:
                    throw new RemoveDockerObjectException("A conflict has been detected.");
                case 500:
                    System.out.println("Server error!");
                    break;
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
