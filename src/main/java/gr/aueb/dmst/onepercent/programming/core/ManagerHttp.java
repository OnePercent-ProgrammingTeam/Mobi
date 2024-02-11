package gr.aueb.dmst.onepercent.programming.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * An abstract class that provides common functionality for managing Docker containers and 
 * images via HTTP requests.
 * 
 * <p>Extends {@link SuperHttp} and implements {@link HttpInterface}.
 * 
 * <p>This is the core management class of the application. It has two concrete subclasses,
 * which are responsible for implementing specific actions, related to docker objects management 
 * such as starting, stopping, removing containers, and pulling images and removing.
 * <ul>
 *   <li>{@link gr.aueb.dmst.onepercent.programming.cli.ManagerHttpCLI} for the CLI version</li>
 *   <li>{@link gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI} for the GUI version</li>
 * </ul>
 * 
 * <p>The logic behind the inheritance is that GUI and CLI versions are built on top of the same
 * core functionality and backend logic, but they have different user interfaces serving different
 * user needs.
 * 
 * <p>Note that the HTTP requests can only be POST and DELETE, as this class is responsible for
 * managing Docker objects and  executing actions on them. No GET requests are made
 * for monitoring purposes, to retrieve information.
 */
public abstract class ManagerHttp extends SuperHttp implements HttpInterface {
    
    /** The entity of the http response, returned by the docker daemon, when a request is made. */
    protected static HttpEntity entity;

    /** Default Constructor. */
    public ManagerHttp() { }

    /** Starts a docker container. */
    public abstract void startContainer();

    /** Stops a docker container. */
    public abstract void stopContainer();

    /** Removes a docker container. */
    public abstract void removeContainer();

    /** Pulls a docker image. */
    public abstract void pullImage();

    /** Removes a docker image. */
    public abstract void removeImage();

    /**
     * Executes the HTTP request.
     * 
     * @param path the path or identifier of the request.
     */
    @Override
    public void executeRequest(String path) {
        try {
            this.http_response = HTTP_CLIENT.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the response of the HTTP request.
     * 
     * @throws IOException if an I/O error occurs while reading the response
     */ 
    public void handleResponse() throws IOException {
        // Create a reader for the response content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity
                                                                              .getContent()))) { 
            String line;
            while ((line = reader.readLine()) != null) { // Print the response line by line
                System.out.println(line);
            }
        }
    }
}
