package gr.aueb.dmst.onepercent.programming.core;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.github.dockerjava.api.DockerClient;


import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;

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

    /**ok */
    protected static HttpDelete deleteRequest;
    /**
     * Http Get request (Get is "to request to get something e.g info about
     * containers")
     */
    protected static HttpGet getRequest;
    /** Image name of the image that is going to be pulled or searched */
    public static String imageName;
    /** Http response return by the executed http request */
    protected CloseableHttpResponse http_response;
    /** Http response, read using StringBuffer */
    public static  StringBuilder response_builder; // used in junit test

    /** Last CPU Usage is the last metric for a running container */
    protected static  double lastCPUUsage;

   
    public String generateResponseMessage(String message) { 
        return message;
    }

    public void printOutput(String message) {
        System.out.println(message);
    }

    /**ok */
    protected static double lastMemoryUsage;


    //for database
    /**The static field "conId" is used to keep the id of the container the user wants to find,  
     * in order to be visible in the database. 
     */
    protected String conId;
    /**
     * ok
     * @return ok
     */
    public String getContainerId() {
        return conId;
    }

    /**
     * ok
     * @param conId ok
     */
    public void setContainerId(String conId) {
        this.conId = conId;
    }

    /**
     * ok
     */
    protected DataBaseThread dataBaseThread = DataBaseThread.getInstance();

    
    /** Execute the http request
     * @param message the message that indicates the action that is going to be executed
     */
    public void executeRequest(String message) {
    };
    /** Get the response of the http request
     *  @return the response that indicates the action that is going to be executed
     * it is the query for the http request
     */
    public CloseableHttpResponse getHttpResponse() {
        return http_response;
    }

    /**
     * ok
     * @return ok
     */
    public StringBuilder getResponse1() {
        return response_builder;
    }

    /**
     * Default Constructor
     */
    public SuperHttp() {

    }
}
