package gr.aueb.dmst.onepercent.programming.core;
/**
 * Interface: HttpInterface is an interface that is implemented by the SuperHttp class.
 * It is used to create a mock object for the MonitorHttp class in the test classes.
 * @see SuperHttp
 */
public interface HttpInterface {
    /**
     * Method: executeHttpRequest(String message) is responsible for executing an HTTP request.
     *
     * @param message the message to be included in the HTTP request
     */
    void executeHttpRequest(String message);
}
