package gr.aueb.dmst.onepercent.programming;
/**
 * Interface: HttpInterface is an interface that is implemented by the SuperHttp class.
 * It is used to create a mock object for the MonitorHttp class in the test classes.
 * @see SuperHttp
 */
public interface HttpInterface {
    void executeHttpRequest(String message);
}
