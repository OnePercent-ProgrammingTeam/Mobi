package gr.aueb.dmst.onepercent.programming;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLRetriever {
    public static void main(String[] args) {
	    if (args.length != 1) {
	        System.err.println("Usage: UrlRetriever URL");
	        System.exit(1);
	    }
        try {
            //Construct the Docker API URL 
            String dockerApiUrl = args[0];

            // Make a request to the Docker API
            URL url = new URL(dockerApiUrl);
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response from the Docker server
            int status = connection.getResponseCode();
            var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            var content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Print the response
            System.out.println(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
