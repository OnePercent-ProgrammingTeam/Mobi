package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.github.dockerjava.api.DockerClient;

/**
 * Class: SuperHttp is a superclass that contains static variables, used in other
 * classes, representing the docker client and the http requests.
 * It is used to create a docker client and the http requests.
 * SuperHttp class implements the HttpInterface interface.
 * It is the superclass of the MonitorHttp class and ManagerHttp class.
 * @see HttpInterface
 * @see MonitorHttp
 * @see ManagerHttp
 */
public class SuperHttp implements HttpInterface {
    /** Field: DOCKER_HOST is the docker host address */
    protected static final String DOCKER_HOST = "http://localhost:2375"; // used in junit test

    /** Field: HTTP_CLIENT is a http client */
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    /** Field: dc is a static variable, used in other classes, representing the docker client */
    protected static DockerClient dc;

    /** Field httpPost request (Post is "to request to do something e.g start container")*/
    protected static HttpPost postRequest;
    /** Container id of the container that is going to be started, stopped or inspected */
    public static String containerId; // used in junit test

    /**
     * Http Get request (Get is "to request to get something e.g info about
     * containers")
     */
    protected static HttpGet getRequest;
    /** Image name of the image that is going to be pulled or searched */
    public static String imageName;
    /** Http response return by the executed http request */
    protected CloseableHttpResponse response;
    /** Http response, read using StringBuffer */
    public static StringBuffer response1; // used in junit test

    /** Last CPU Usage is the last metric for a running container */
    protected static double lastCPUUsage;

    /** Execute the http request
     * @param message the message that indicates the action that is going to be executed
     */
    public void executeHttpRequest(String message) {
    };
    /** Get the response of the http request
     *  @return the response that indicates the action that is going to be executed
     * it is the query for the http request
     */
    public CloseableHttpResponse getHttpResponse() {
        return response;
    }

    /**
     * Default Constructor
     */
    public SuperHttp() {

    }
}
