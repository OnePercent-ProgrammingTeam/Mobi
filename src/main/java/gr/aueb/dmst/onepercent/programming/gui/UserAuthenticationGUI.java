package gr.aueb.dmst.onepercent.programming.gui;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import gr.aueb.dmst.onepercent.programming.core.UserAuthentication;

/**
 * ok
 */

public class UserAuthenticationGUI extends UserAuthentication {
    /**ok */
    public UserAuthenticationGUI() { }

    /**
     * ok
     * @param username ok
     * @param password ok
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void checkAuth() {
        String message = "check";
        
        postRequest = new HttpPost("https://hub.docker.com/v2/users/login");

        String jsonBody = "{\"username\": \"" + username 
                        + "\", \"password\": \"" + password + "\"}";
        postRequest.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        executeRequest(message);
    }



    /** Method handleOutput(String) prints the appropriate message, based on the status 
     *  code of the http response and the request that has been done.
     * @param message the message that indicates the action that is going to be executed. 
     */
    @Override
    public void handleOutput() {
        if (this.http_response == null) {
            return;
        } else {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 200: 
                    userExists = true;
                    break;
                case 401: 
                    userExists = false;
                    break;
            }
        } 
    }
}
