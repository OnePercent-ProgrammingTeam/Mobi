package gr.aueb.dmst.onepercent.programming.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;


import gr.aueb.dmst.onepercent.programming.core.ManagerHttp;

/**
 * ManagerHttpGUI extends ManagerHttp and provides GUI-specific functionality 
 * for managing Docker containers
 * using HTTP requests.
 *
 * This class allows starting and stopping containers with HTTP requests without displaying messages
 * to the command line. The pullImage method is not implemented in this class.
 *
 * @see ManagerHttp
 */
public class ManagerHttpGUI extends ManagerHttp {
    
    /**
     * Default constructor for ManagerHttpGUI.
     */
    public ManagerHttpGUI() { }

    /** Method: startContainerGUI() starts container with http request 
     * without showing messages to command line. 
     */
    @Override
    public void startContainer() {
        String message = "start";
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + this.containerId + "/" + message);
        executeHttpRequest(message);
    }
    
    /** Method: stopContainerGUI() stops container with http request
     * without showing messages to command line. 
     */
    @Override
    public void stopContainer() {
        String message = "stop";
        postRequest = new HttpPost(DOCKER_HOST + "/containers/" + containerId + "/" + message);
        executeHttpRequest(message);
    }

    @Override
    public void pullImage() { 
        String message = "pull";
        postRequest = new HttpPost(DOCKER_HOST + "/images/create?fromImage=" + imageName);
        executeHttpRequest(message);
    }

    @Override
    public void removeContainer() {
        String message = "remove";
        //TO DO: sundesh me database
        conId = containerId;
        deleteRequest = new HttpDelete(DOCKER_HOST + 
                                      "/containers/" + 
                                      containerId + "?force=true");
        executeHttpRequest(message);
    }
    
    

    @Override 
    public void removeImage() {
        String message = "removeImg";
        dataBaseThread.setImageName(imageName);
        deleteRequest = new HttpDelete(DOCKER_HOST + "/images/" + this.imageName);
        executeHttpRequest(message);
    }

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
                    return;
            }
            entity = response.getEntity();   
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(entity.getContent()));
            String inputLine;
            ManagerHttpGUI.response1 = new StringBuilder(); 
            if (message.equals("pull")) {
                ManagerHttpGUI.response1.append(response.getStatusLine().getStatusCode());
            }  else {
                while ((inputLine = reader.readLine()) != null) {
                    response1.append(inputLine);
                }
            }  
            reader.close();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace of the error
        } finally {
            if (!(message.equals("remove") || message.equals("removeImg")))
                EntityUtils.consumeQuietly(postRequest.getEntity()); 
        }
    }
}
