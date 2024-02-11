package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;

import com.github.dockerjava.api.DockerClient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * A class that contains static variables used by other classes
 * to interact with Docker Engine API via HTTP requests.
 * 
 * <p>It is the superclass of the {@link MonitorHttp} and {@link ManagerHttp} classes.
 * 
 * @see gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI
 * @see gr.aueb.dmst.onepercent.programming.cli.ManagerHttpCLI
 * @see gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI
 * @see gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI
 */
public class SuperHttp {
    
    /** Identifier for the Docker container. */
    public static String containerId; 
    /** Image name for Docker operations. */
    public static String imageName;
    /** HTTP response content stored as a StringBuilder. */
    public static  StringBuilder response_builder; //Used in JUnit tests
    /** The Docker daemon host URL. */
    public static final String DOCKER_HOST = "http://localhost:2375"; //Used in JUnit tests
    /** The HTTP client for executing HTTP requests. */
    protected static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
    /** The Docker client instance. */
    protected static DockerClient dc;
    /** HTTP POST request object. */
    protected static HttpPost postRequest;
    /** HTTP DELETE request object. */
    protected static HttpDelete deleteRequest;
    /** HTTP GET request object. */
    protected static HttpGet getRequest;
    /** HTTP response object. */
    protected CloseableHttpResponse http_response;
    /** Last CPU usage metric for a running container. */
    protected static double lastCPUUsage;
    /** Last memory usage metric for a running container. */
    protected static double lastMemoryUsage;
    /** The container ID stored for database operations. */
    protected String conId; //Used in database
    /** Singleton instance of the database thread. */
    protected DataBaseThread dataBaseThread = DataBaseThread.getInstance();

    /** Default constructor. */
    public SuperHttp() { }

    /**
        * Generates a response message.
        * 
        * @param message The message to be generated.
        * @return The generated response message.
        */
    public String generateResponseMessage(String message) { 
        return message;
    }

    /**
    * Prints output message to the console.
    * 
    * @param message The message to be printed.
    */
    public void printOutput(String message) {
        System.out.println(message);
    }

    /**
        * Retrieves the container ID.
        * 
        * @return The container ID.
        */
    public String getContainerId() {
        return conId;
    }

    /**
        * Sets the container ID.
        * 
        * @param conId The container ID to be set.
        */
    public void setContainerId(String conId) {
        this.conId = conId;
    }

    /**
        * Executes an HTTP request.
        * 
        * @param message The message indicating the action to be executed.
        */
    public void executeRequest(String message) {
    };

    /**
        * Retrieves the HTTP response.
        * 
        * @return The HTTP response.
        */
    public CloseableHttpResponse getHttpResponse() {
        return http_response;
    }

    /**
        * Retrieves the HTTP response content stored as a StringBuilder.
        * 
        * @return The HTTP response content as a StringBuilder.
        */
    public StringBuilder getResponse1() {
        return response_builder;
    }

    /**
        * Default constructor for the SuperHttp class.
        */
}
