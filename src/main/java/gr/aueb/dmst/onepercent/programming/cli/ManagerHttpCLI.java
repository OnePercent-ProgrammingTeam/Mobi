package gr.aueb.dmst.onepercent.programming.cli;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.GREEN;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import gr.aueb.dmst.onepercent.programming.core.ManagerHttp;
import gr.aueb.dmst.onepercent.programming.exceptions.ActionContainerException;
import gr.aueb.dmst.onepercent.programming.exceptions.PullImageException;
import gr.aueb.dmst.onepercent.programming.exceptions.RemoveDockerObjectException;

import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 * A manager class responsible for executing functionalities related to Docker containers 
 * and images management.
 * 
 * <p>This class is used in the CLI version of the application and extends
 * {@link gr.aueb.dmst.onepercent.programming.core.ManagerHttp}.
 * It provides methods to execute HTTP requests to the
 * <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API v1.43</a> in order 
 * to perform various actions. These actions typically involve creating, starting, 
 * stopping, removing containers, or pulling and removing images.
 * 
 * <p>Note that HTTP requests made by this class are typically POST and DELETE requests for
 *  executing tasks, such as creating or removing resources. The retrieval of information, 
 *  which involves GET requests, is implemented in classes related to monitoring Docker resources.
 *  @see gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI
 */
public class ManagerHttpCLI extends ManagerHttp {
    
    /** Default Constructor. */
    public ManagerHttpCLI() { }

    /** Starts a container with POST HTTP request. */
    @Override
    public void startContainer() {
        String path = "start";
        containerId = ConsoleUnits.promptForInput(
            "Please type the container ID to start the container: ");
        conId = containerId;
        postRequest = new HttpPost(DOCKER_HOST.concat("/containers/")
                                              .concat(containerId)
                                              .concat("/")
                                              .concat(path));
        executeRequest(path);
    }

    /** Stops a container with POST HTTP request. */
    @Override
    public void stopContainer() {
        String path = "stop";
        containerId = ConsoleUnits.promptForInput(
            "Please type the container ID to stop the container: ");
        conId = containerId;
        postRequest = new HttpPost(DOCKER_HOST.concat("/containers/")
                                              .concat(containerId)
                                              .concat("/")
                                              .concat(path));
        executeRequest(path);
    }
    
    /** Removes a container with DELETE HTTP request. */
    @Override
    public void removeContainer() {
        String identifier = "remove";
        containerId = ConsoleUnits.promptForInput(
            "Please type the container ID to remove the container: ");
        conId = containerId;
        deleteRequest = new HttpDelete(DOCKER_HOST.concat("/containers/")
                                                  .concat(containerId)
                                                  .concat("?force=true"));
        executeRequest(identifier);
    }

    /** Pulls an image with POST HTTP request. */
    @Override
    public void pullImage() {
        String identifier = "pull";
        imageName = ConsoleUnits.promptForInput(
                "Please type the name of the image you would like to pull:");
        dataBaseThread.setImageName(imageName);
        postRequest = new HttpPost(DOCKER_HOST.concat("/images/create?fromImage=")
                                              .concat(imageName));
        executeRequest(identifier);
    }

    /** Removes an image with DELETE HTTP request. */
    @Override 
    public void removeImage() {
        String identifier = "removeImg";
        imageName = ConsoleUnits.promptForInput(
            "Please type the ID of the image you would like to remove:");
        dataBaseThread.setImageName(imageName);
        deleteRequest = new HttpDelete(DOCKER_HOST + "/images/" + imageName);
        executeRequest(identifier);
    }

    /** Executes the HTTP request.
     * It is the central-helper method that executes the HTTP requests.
     * @param message the message that indicates the action that is going to be executed.
     */
    @Override
    public void executeRequest(String message) {
        System.out.println("Please wait...");
        try {
            switch (message) {
                case "start":
                case "stop":
                case "pull":
                    this.http_response = HTTP_CLIENT.execute(postRequest);
                    break;
                case "remove":
                case "removeImg":
                    this.http_response = HTTP_CLIENT.execute(deleteRequest);
                    break;
            }
            entity = this.http_response.getEntity();     
            printOutput(message);     
        } catch (IOException e) {
            System.out.println(RED + "Oops, an input-output error has occured..." + RESET);
        } finally {
            //Consume the entity to release the resources of the response of the post request
            if (!(message.equals("remove") || message.equals("removeImg")))
                EntityUtils.consumeQuietly(postRequest.getEntity()); 
        }
    }

    /**
     * Prints the modified output, indicating the result of the action.
     * The output indicates whether the action was successful or not.
     * @param message The message that indicates the action that is going to be executed.
     * @return  The output message based on the HTTP response status code and the input message.
     */
    public void printOutput(String message) {
        String output = "";
        if (this.http_response == null) {
            output = "Response has not been initialized.";
        } else {
            try {
                output = generateResponseMessage(message);
            } catch (ActionContainerException e) {
                output = e.getMessage();
            } catch (PullImageException e) {
                output = e.getMessage();
            } catch (RemoveDockerObjectException e) {
                output = e.getMessage();
            }
        }
        System.out.println(output);
    }

    /**
     * Retrieves the message indicating the result of the action for testing purposes.
     * @param message The message that indicates the action that is going to be executed.
     * @return The generated output message based on the response status code and the input message.
     */
    @VisibleForTesting
    public String getgeneratedResponseMessage(String message) {
        return generateResponseMessage(message);
    }

    /**
     * Generates the output message based on the HTTP response status code and the input message.
     * Based on the HTTP response status code and the input message, it throws the 
     * appropriate customized exceptions and returns the customized message.
     * 
     *<p>Note that the methods called on the database thread are used to update the database and
     * used later for the history/log of the application.
     * @param message The message that indicates the action that is going to be executed.
     * @return The generated output message 
     * based on the HTTP response status code and the input message.
     * @throws ActionContainerException if the container is already started or stopped, not found, 
     * or if there is a server error.
     * @throws PullImageException if the image is not found or there is a server error.
     * @throws RemoveContainerException due to bad parameter,
     * conflict, server error, or trial to remove a non-existing object.
     */
    public String generateResponseMessage(String message) throws ActionContainerException,
                                                        PullImageException,
                                                        RemoveDockerObjectException {
        String output = "";
        if (message.equals("start") || message.equals("stop")) {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 204:
                    dataBaseThread.setState("success");
                    output = GREEN + "Container " + message  + " was successfull." +
                        RESET;
                    break;
                case 304:
                    dataBaseThread.setState("failure");
                    message = (message.equals("start")) ? "start" : "stopp";
                    throw new ActionContainerException("Container already " + message + "ed.");
                case 404:
                    dataBaseThread.setState("failure");
                    throw new ActionContainerException("There is no such container. Try again.");
                case 500:
                    dataBaseThread.setState("failure");
                    throw new ActionContainerException("Server error!");
            }
        } else if (message.equals("pull")) {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 200:
                    dataBaseThread.setState("success");
                    output = GREEN + "Image " + message + " was successfull."
                           + RESET;
                    break;
                case 404:
                    dataBaseThread.setState("failure");
                    throw new PullImageException("Image not found.");
                case 500:
                    dataBaseThread.setState("failure");
                    throw new PullImageException("Server error!");
            }
        } else if (message.equals("remove") || message.equals("removeImg")) {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 200:
                case 204:
                    dataBaseThread.setState("success");
                    output = GREEN + "Successfull removal." + RESET;
                    break;
                case 400:
                    dataBaseThread.setState("failure");
                    throw new RemoveDockerObjectException("Bad parameter.");
                case 404:
                    dataBaseThread.setState("failure");
                    throw new RemoveDockerObjectException("No such object found. Try again");
                case 409:
                    dataBaseThread.setState("failure");
                    throw new RemoveDockerObjectException("A conflict has been detected.");
                case 500:
                    dataBaseThread.setState("failure");
                    System.out.println("Server error!");
                    break;
            }
        }
        return output;
    }
}
