package gr.aueb.dmst.onepercent.programming.core;

/**
 * A simple interface for HTTP requests.
 * 
 * <p>An interface to execute an HTTP request with the given message, which is
 * a string identifier, being a part of the path sent to the server.
 *  
 * <p>It is implemented by the {@link SuperHttp} class.
 * 
 * @see SuperHttp
 */
public interface HttpInterface {

    /**
     * Executes an HTTP request with the given message.
     *
     * @param message the message to be included in the HTTP request path or to
     * serve as a identifier for the request.
     */
    void executeRequest(String path);
}
