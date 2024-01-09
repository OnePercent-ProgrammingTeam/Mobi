package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.HttpPost;

public class ManagerHttpGUI extends ManagerHttp {
    
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
