package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.HttpPost;

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
    public void pullImage() { }
}
