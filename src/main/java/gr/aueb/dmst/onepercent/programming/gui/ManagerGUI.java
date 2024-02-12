package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.core.Manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 * A manager class responsible for executing functionalities related to Docker containers 
 * and images management.
 * 
 * <p>This class is used in the GUI version of the application and extends
 * {@link gr.aueb.dmst.onepercent.programming.core.Manager}.
 * It provides methods to execute HTTP requests to the
 * <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API v1.43</a> in order 
 * to perform various actions. These actions typically involve creating, starting, 
 * stopping, removing containers, or pulling and removing images.
 * 
 * <p>Note that HTTP requests made by this class are typically POST and DELETE requests for
 *  executing tasks, such as creating or removing resources. The retrieval of information, 
 *  which involves GET requests, is implemented in classes related to monitoring Docker resources.
 *  @see gr.aueb.dmst.onepercent.programming.cli.MonitorCLI
 */
public class ManagerGUI extends Manager {
    
    //TO DO(Scobioala-Selar): Add functionality that let's user connect a container to a network.

    /**
     * Default constructor for ManagerHttpGUI.
     */
    public ManagerGUI() { }

    /** Starts a container with POST HTTP request. */
    @Override
    public void startContainer() {
        String message = "start";
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + this.containerId + "/" + message);
        executeRequest(message);
    }
    
    /** Stops a container with POST HTTP request. */
    @Override
    public void stopContainer() {
        String message = "stop";
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeRequest(message);
    }

    /** Removes a container with DELETE HTTP request. */
    @Override
    public void removeContainer() {
        String message = "remove";
        //TO DO(Dionisia Koronellou): Connections with database.
        conId = containerId;
        deleteRequest = new HttpDelete(DOCKER_HOST + 
                                        "/containers/" + 
                                        containerId + "?force=true");
        executeRequest(message);
    }

    /** Pulls an image with POST HTTP request. */
    @Override
    public void pullImage() { 
        String message = "pull";
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeRequest(message);
    }

    /** Removes an image with DELETE HTTP request. */
    @Override 
    public void removeImage() {
        String message = "removeImg";
        dataBaseThread.setImageName(imageName);
        deleteRequest = new HttpDelete(DOCKER_HOST + "/images/" + this.imageName);
        executeRequest(message);
    }

    /** Executes the HTTP request.
     * It is the central-helper method that executes the HTTP requests.
     * @param message the message that indicates the action that is going to be executed.
     */
    @Override
    public void executeRequest(String message) {
        try {
            switch (message) {
                case "start":
                case "stop":
                    this.http_response = HTTP_CLIENT.execute(postRequest);
                    break;
                case "pull":
                    this.http_response = HTTP_CLIENT.execute(postRequest);
                    ManagerGUI.response_builder = new StringBuilder(); 
                    ManagerGUI.response_builder
                                  .append(http_response.getStatusLine().getStatusCode());
                    return;
                case "remove":
                case "removeImg":
                    this.http_response = HTTP_CLIENT.execute(deleteRequest);
                    return;
            }
            entity = http_response.getEntity();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(entity.getContent()));
            String inputLine;
            ManagerGUI.response_builder = new StringBuilder(); 
            if (message.equals("pull")) {
                //TO DO(Selar - Scobioala): Add ui components
            }  else {
                while ((inputLine = reader.readLine()) != null) {
                    response_builder.append(inputLine);
                }
            }  
            reader.close();
        } catch (NullPointerException e) {
            System.out.println(""); //Entity is null when executing POST / DETELE requests.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!(message.equals("remove") || message.equals("removeImg")))
                EntityUtils.consumeQuietly(postRequest.getEntity()); 
        }
    }
}
