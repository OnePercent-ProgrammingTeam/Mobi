package gr.aueb.dmst.onepercent.programming.core;


import org.apache.http.util.EntityUtils;

import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;

import org.apache.http.HttpEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Class: ManagerHttp is responsible for the http requests that are made to the docker daemon
 *  in order to start/stop a container or pull an image.
 *  It extends the SuperHttp class.
 *  @see SuperHttp
 */
public abstract class ManagerHttp extends SuperHttp {
    
    /**
     * The entity of the http response.
     */
    protected static HttpEntity entity;

    /**
     * Method: startContainer() - Abstract method to start a container.
     * Implementing classes should provide the necessary logic 
     * to initiate the start of a Docker container.
     */
    public abstract void startContainer();

    /**
     * Method: stopContainer() - Abstract method to stop a container.
     * Implementing classes should provide the necessary logic 
     * to initiate the stop of a Docker container.
     */
    public abstract void stopContainer();

    /**
     * Method: pullImage() - Abstract method to pull a Docker image.
     * Implementing classes should provide the necessary logic 
     * to download a Docker image from a registry.
     */
    public abstract void pullImage();

    /**
     * ok
     */
    public abstract void removeContainer();

    /**
     * ok
     */
    public abstract void removeImage();
    
    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * You should add try-catch block to use @ for
     * throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeRequest(String message) {
        try {
            this.http_response = HTTP_CLIENT.execute(postRequest); // Start the container
            entity = http_response.getEntity();   
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(entity.getContent()));
            String inputLine;
            ManagerHttpGUI.response_builder = new StringBuilder(); 
            if (message.equals("pull")) {
                ManagerHttpGUI.response_builder
                              .append(http_response.getStatusLine().getStatusCode());
            }  else {
                while ((inputLine = reader.readLine()) != null) {
                    response_builder.append(inputLine);
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
    
    //TO DO: Check if it is needed, at this moment it might be useless
    /**
    * Method: handleResponse() reads and prints the content of an input stream line by line.
    *
    * This method takes an input stream, wraps it with a BufferedReader, and reads its content
    * line by line. Each line is then printed to the console. The BufferedReader is automatically
    * closed (It is autoclosable. That is why we use the parenthesis in try segment of try-catch
    * statement. It would be the same, if we had a finally statement, in which we would "close"
    * with .close() method the 
    * BufferedReader object) upon exiting the method, due to the try-with-resources statement.
    *
    * @throws IOException If an I/O error occurs while reading the input stream.
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

    /**
     * Default Constructor
     */
    public ManagerHttp() {

    }
}
