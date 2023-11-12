import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import io.netty.handler.codec.http.HttpResponse;

public class ContainerManagerHttp {
    
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // Create an HTTP client

        String dockerHost = "http://localhost:2375"; // This should be the docker host IP
        String containerId = "ad73ec4df3f3a3a058c3e55f3d29fb2fe0e92396897a8a8b48cfbcfbcb1c178e"; // test-mongo container

        HttpPost request = new HttpPost(dockerHost + "/containers/" + containerId + "/start"); //might need to change the path and query parameters

        try {
            CloseableHttpResponse response = httpClient.execute(request);
             // Start the container
            HttpEntity entity = response.getEntity(); // Get the response entity
            System.out.println("Container started successfully.");
            if (entity != null) { // If the response entity is not null, print it on the console
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) { // Create a reader for the response content
                    String line;
                    while ((line = reader.readLine()) != null) { // Print the response line by line
                        System.out.println(line);
                     }
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start the container: " + e.getMessage()); // Print the error message
        } finally {
            EntityUtils.consumeQuietly(request.getEntity()); // Release the resources of the request
        }
    }
}

