package gr.aueb.dmst.onepercent.programming;

import java.io.Closeable;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class ContainerHttpRequest {
    /** Docker host address*/ 
    protected static final String DOCKER_HOST = "http://localhost:2375";

    /** Create an http client */
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault(); 

    /** Http Post request (Post is "do to something e.g start container") */
    protected static HttpPost postRequest; 
    protected static String containerId;

    /** Http Get request (Get is "to get something e.g info about containers") */
    protected static HttpGet getRequest;
    protected static String imageName;

    protected CloseableHttpResponse response;
    
    public abstract void executeHttpRequest(String message);

    public CloseableHttpResponse getHttpResponse(String message){
        return response;
    }

}
