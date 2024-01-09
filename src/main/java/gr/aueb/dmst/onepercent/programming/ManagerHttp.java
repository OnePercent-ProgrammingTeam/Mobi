package gr.aueb.dmst.onepercent.programming;


import org.apache.http.util.EntityUtils;
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
    
    static HttpEntity entity;
    
    public abstract void startContainer();

    public abstract void stopContainer();
    
    public abstract void pullImage();
    
    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * @throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            this.response = HTTP_CLIENT.execute(postRequest); // Start the container
            entity = response.getEntity();            
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
    * @param entity The HTTP response entity containing the input stream to be read.
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

    
}
