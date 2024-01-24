package gr.aueb.dmst.onepercent.programming.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

    //TO DO: implement removeContainer() & removeImage() 
    //method for the GUI version of the application
    //Check out the CLI version of the application for reference
    @Override 
    public void removeContainer() { }

    @Override
    public void removeImage() { }

    @Override
    public void executeHttpRequest(String message) {
        try {
            this.response = HTTP_CLIENT.execute(postRequest); // Start the container
            if (message.equals("start") || message.equals("stop")) {
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
}
