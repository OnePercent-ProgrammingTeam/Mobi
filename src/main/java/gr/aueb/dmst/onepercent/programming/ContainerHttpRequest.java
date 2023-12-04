package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class ContainerHttpRequest implements HttpInterface {
    /** Docker host address*/ 
    protected static final String DOCKER_HOST = "http://localhost:2375"; //used in junit test

    /** Create an http client */
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault(); 

    /** Http Post request (Post is "to request to do something e.g start container") */
    protected static HttpPost postRequest; 
    protected static String containerId; //used in junit test

    /** Http Get request (Get is "to request to get something e.g info about containers") */
    protected static HttpGet getRequest;
    protected static String imageName;

    protected CloseableHttpResponse response;
    protected static StringBuffer response1; //used in junit test

    /** Last CPU Usage is the last metric for a running container */
    protected static double lastCPUUsage;

    //This will be implemented by children of the class 
    public void executeHttpRequest(String message){};

    public CloseableHttpResponse getHttpResponse(String message){
        return response;
    }
}
