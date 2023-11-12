import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ContainerManagerHttp {
    
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // Create an HTTP client

        String dockerHost = "http://denKserwAkomaToPath"; // This should be the docker host IP
        String containerId = "ad73ec4df3f3a3a058c3e55f3d29fb2fe0e92396897a8a8b48cfbcfbcb1c178e"; // test-mongo container

        HttpPost request = new HttpPost(dockerHost + "/containers/" + containerId + "/start"); //might need to change the path and query parameters

        try {
            httpClient.execute(request); // Start the container

            System.out.println("Container started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start the container: " + e.getMessage()); // Print the error message
        } finally {
            EntityUtils.consumeQuietly(request.getEntity()); // Release the resources of the request
        }
    }
}

