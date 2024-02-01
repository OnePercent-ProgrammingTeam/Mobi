package gr.aueb.dmst.onepercent.programming.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;


/**
 * Class: MonitorHttpCLI extends MonitorHttp
 * 
 * Description: Provides CLI-specific implementation 
 * for monitoring Docker containers through HTTP requests.
 * @see gr.aueb.dmst.onepercent.programming.core.MonitorHttp
 * @see gr.aueb.dmst.onepercent.programming.core.Graph
 */
public class MonitorHttpCLI extends MonitorHttp {
    
    final String RESET = "\u001B[0m";
    final String BLUE = "\u001B[34m";
    final String RED = "\u001B[31m";
    final String GREEN = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    
    /** 
     * Retrieves information about a container by
     * sending an HTTP GET request to the Docker daemon.
    */
    @Override
    public void inspectContainer() {
        String path = "json"; // get the container information in json format
        MonitorHttp.containerId = Main.handleInput(
            "Please type the container ID to get info about the container: ");
        conId = containerId;
        getRequest = new HttpGet(DOCKER_HOST.concat("/containers/")
                                            .concat(MonitorHttp.containerId)
                                            .concat("/").concat(path));                       
        executeRequest(path);
        //check other situations with the state
        dataBaseThread.setState("success");
        printContainerInfo();
    }

    /**
     * Searches for images, uploaded in Docker Hub,
     * by sending an HTTP GET request to the Docker daemon.
     */
    @Override
    public void searchImage() {
        String path = "/images/search"; // get the container statistics in json format
        imageName = Main.handleInput("Please type the name of the image you want to search for: ");
        dataBaseThread.setImageName(imageName);
        getRequest = new HttpGet(DOCKER_HOST.concat(path)
                                            .concat("?term=")
                                            .concat(imageName)
                                            .concat("&limit=3"));
        executeRequest(path);
        //check other situations with the state
        dataBaseThread.setState("success");
        printSearchResults();
    }

    /*
     * Lists the images that are installed in the system by
     * sending an HTTP GET request to the Docker daemon.
     */
    public void listImages() {
        String path = "/images/json";
        getRequest = new HttpGet(DOCKER_HOST.concat(path).concat("?all=true&shares-size=true"));
        executeRequest(path);
    }

    /**
     * Retrieves information about the Docker System by
     * sending an HTTP GET request to the Docker daemon.
     */
    public void systemInfo() {
        String path = "/info";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
        printSystemInfo();

    }

    /**
     * Retrieves information about the Docker Version by
     * sending an HTTP GET request to the Docker daemon.
     */
    public void dockerVersion() {
        String path = "/version";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
        printDockerVersion();
    }

    public StringBuilder formatSwarmInfo() {
        StringBuilder swarmInfo = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response1.toString());
            
            swarmInfo.append("\nSwarm Name: " + jn.get("Spec").get("Name").asText() + "\n");
            swarmInfo.append("Swarm ID: ".concat(jn.get("ID").asText()).concat("\n"));
            swarmInfo.append("Version: " + jn.get("Version").get("Index").asText() + "\n");
            swarmInfo.append("Created at: ".concat(
                                                    jn.get("CreatedAt")
                                                      .asText()
                                                      .replace("T", " ")
                                                      .substring(0, 19)
                                                    ).concat("\n"));
            swarmInfo.append("Updated at: " + jn.get("UpdatedAt")
                                                    .asText()
                                                    .replace("T", " ")
                                                    .substring(0, 19) + "\n");
            swarmInfo.append("Subnetwork size: " + jn
                                                    .get("SubnetSize")
                                                    .asText() + "\n");
            /*if the 'Worker' node is null, then there are no workers in the swarm.
            *Therefore, the opposite means that there is at least one worker in the swarm.
            *The same applies for the 'Manager' node.*/
            if (!(jn.get("JoinTokens").get("Worker").isNull())) {
                swarmInfo.append("There is at least one worker in the swarm\n");
            } 
            if (!(jn.get("JoinTokens").get("Manager").isNull())) {
                swarmInfo.append("There is at least one manager in the swarm\n");
            }
            return swarmInfo;
        } catch (Exception e) {
            System.out.println("Some information is missing...");
        }
        return swarmInfo.append(" ");
    }

    /** Method: getContainerStats(String) gets statistics about a running container 
     *  (especially CPU usage). 
     *  @param calledby the name of the class that called this method.
     *  These stats are used in Graph class in order to create a graph.
    */
    @Override
    public CloseableHttpResponse getContainerStats(String calledby) {
        String message = "stats"; // get the container statistics in json format
        String outputMessage;
        if (calledby.equals("Graph")) {
            outputMessage = "Please type the container ID to plot diagram with CPU usage: ";
        } else {
            outputMessage = "Please type the ID of the running container to save real time data: ";
        }
        MonitorHttp.containerId = Main.handleInput(outputMessage);
        conId = containerId;
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                 "/containers/" + 
                                 MonitorHttp.containerId + 
                                 "/" + message);                        
        return getHttpResponse();
    }

    /**
     * Prints the information about a container.
     * The information contains:
     * 1. Container Name
     * 2. Container ID
     * 3. Status
     * 4. Image ID
     * 5. Network ID
     * 6. Gateway
     * 7. IP Address
     * 8. Mac Address
     */
    public void printContainerInfo() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response1.toString());
            System.out.println("Container Name: ".concat(jn.get("Name").asText().substring(1)));
            System.out.println("Container ID: ".concat(jn.get("Id").asText()));
            System.out.println("Status: ".concat(jn.get("State").get("Status").asText()));
            System.out.println("Image ID: ".concat(jn.get("Image").asText()));
            System.out.println("Network ID: ".concat(jn.get("NetworkSettings")
                                                  .get("Networks")
                                                  .get("bridge")
                                                  .get("NetworkID").asText())); 
            System.out.println("Gateway: ".concat(jn.get("NetworkSettings")
                                               .get("Networks")
                                               .get("bridge")
                                               .get("Gateway").asText())); 
            System.out.println("IP Address: ".concat(jn.get("NetworkSettings")
                                                  .get("Networks")
                                                  .get("bridge")
                                                  .get("IPAddress").asText())); 
            System.out.println("Mac Address: ".concat(jn.get("NetworkSettings")
                                                   .get("Networks")
                                                   .get("bridge")
                                                   .get("MacAddress").asText())); 
            String imName = (jn.path("Config/Labels/org.opencontainers.image.title")
                                .isMissingNode()) ? "N/A" : 
                                jn.path("Config/Labels/org.opencontainers.image.title").asText();
            
            System.out.println("Image Name: ".concat(imName));
        } catch (NullPointerException e) {
            System.out.println("Exception due to null value");
        } catch (JsonProcessingException e) {
            System.out.println("Exception due to JsonProcessing");
        }
    }



    /** executeRequest(String) executes the http request for 
     *  getting info about a container.
     * @param path the final part of the url that is used to get the info.
     * may throw Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeRequest(String path) {
        try { 
            this.response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.response.getEntity().getContent()));
            String inputLine;
            MonitorHttp.response1 = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response1.append(inputLine);
                if (path.equals("stats")) {
                    lastCPUUsage = getCPUusage(response1); //print real time CPU Usage
                    //dataBaseThread.setState("success");
                    lastMemoryUsage = getMemoryUsage(response1); 
                    this.response.close();
                    break;
                }
            }
            reader.close();    
        } catch (NullPointerException e) {
            System.out.println("Oops, something went wrong...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException Occured while executing the http request");
        }
    }

    /**
     * prints the the results of the search
     * The results contain:
     * 1. Image Name
     * 2. Description
     * 3. Star Count (the times an image has been shared)
     */
    public void printSearchResults() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response1.toString()); // could use .body()
                
            if (jsonNode.isArray()) {
                System.out.println("Top 3 searched results\n");
                for (JsonNode el : jsonNode) {
                    System.out.println("Image name: " + el.get("name") 
                        + "\nDescription: " 
                        + el.get("description") 
                        + "\nStar count: " 
                        + el.get("star_count") + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Oops, something went wrong...");
        }
    }

    /**
     * Default Constructor
     */
    public MonitorHttpCLI() {

    }

    /**
     * Estimates the number of images that will be displayed, based on the user's input.
     */
    public void estimateImages() {
        //TO DO: check if the input is a number, if the 
        //input is beyond the number of images installed in the system
        //and print the number of locally installed images 
        //System.out.println("You have ... images installed in your system.");
        System.out.print("Define the number of images you want to be displayed: ");
    }
    

    /**
     * Prints the list of images that will be displayed, based on the user's input.
     * @param images the number of images that will be displayed.
     * @throws JsonProcessingException if there is an error processing the JSON response.
     * @throws NullPointerException if there is a null value in the JSON response.
     */
    public void printImagesList(int images) {
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response1.toString());
            
            System.out.println("-IMAGES-\n");
            System.out.printf("%-40s %-60s", "Image Name", "Image ID");
            System.out.println(" ");
            
            for (int i = 0; i < images; i++) {
                String name = jn.get(i).get("RepoTags").isEmpty() ? "N/A" : 
                        jn.get(i).get("RepoTags").get(0).asText();
                name = (name.length() > 35) ? name.substring(0, 35).concat("...") : name;

                String id = jn.get(i).get("Id").asText();
                
                /*just make it a little bit more beautiful  by coloring the sha256 hash,
                  a cryptographic hash function that produces a fixed-size (256-bit) 
                  string of characters - the id of the image*/
                id = id.replace(id.substring(0, 6), BLUE + id.substring(0, 6) + RESET);
                
                System.out.printf("\n%-40s %-60s", name, id);
            }
        
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Some information is missing..." + RESET);
        } catch (NullPointerException e) {
           
            System.out.println(RED + "Something when wrong." + RESET);
        }
    }

    /**
     * Prints the Docker System information. It is called by the method systemInfo().
     */
    public void printSystemInfo() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response1.toString());
            System.out.println("\n".concat(BLUE).concat("SYSTEM").concat(RESET));
            System.out.println("Identification: ".concat(jn.get("ID").asText()));
            System.out.println("Operating System: ".concat(jn.get("OperatingSystem").asText()));
            System.out.println("OSType: ".concat(jn.get("OSType").asText()));
            System.out.println("OS Version: ".concat(jn.get("OSVersion").asText()));
            System.out.println("\n".concat(BLUE).concat("RESOURCES").concat(RESET));
            System.out.println("Total Memory: ".concat(jn.get("MemTotal").asText()));
            System.out.println("Total CPUs: ".concat(jn.get("NCPU").asText()));
            System.out.println("Total Containers: ".concat(jn.get("Containers").asText()));
            System.out.println("\n".concat(BLUE).concat("IMAGES").concat(RESET));
            System.out.println("Total Images: ".concat(jn.get("Images").asText()));
            System.out.println("\n".concat(BLUE).concat("CONTAINERS").concat(RESET));
            System.out.println("Running Containers: ".concat(jn.get("ContainersRunning").asText()));
            System.out.println("Paused Containers: ".concat(jn.get("ContainersPaused").asText()));
            System.out.println("Stopped Containers: ".concat(jn.get("ContainersStopped").asText()));
            System.out.println("\n".concat(BLUE).concat("SWARM").concat(RESET));
            System.out.println("Node ID: ".concat(jn.get("Swarm").get("NodeID").asText()));
            System.out.println("Node Adress: ".concat(jn.get("Swarm").get("NodeAddr").asText()));
            String color;
            color = jn.get("Swarm").get("LocalNodeState").asText().equals("active") ? GREEN : RED;
            System.out.println("Status: ".concat(color)
                                         .concat(
                                                    jn.get("Swarm").get("LocalNodeState").asText()
                                                ).concat(RESET));

            System.out.println("\n".concat(RED).concat("WARNINGS").concat(RESET));
            
            if (jn.get("Warnings").isArray()) {
                for (JsonNode node : jn.get("Warnings")) {
                    System.out.println(YELLOW.concat(node.asText()).concat(RESET));
                }
            } else {
                System.out.println(YELLOW.concat(jn.get("Warnings").asText()).concat(RESET));
            }

        } catch (Exception e) {
            System.out.println("Some information is missing...");
        }
    }

    /*
     * Prints the Docker Version information. It is called by the method dockerVersion().
     */
    public void printDockerVersion() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response1.toString());
            System.out.println("\n".concat(BLUE).concat("DOCKER VERSION").concat(RESET));
            System.out.println("Version: ".concat(jn.get("Version").asText()));
            System.out.println("API Version: ".concat(jn.get("ApiVersion").asText()));
            System.out.println("Min API Version: ".concat(jn.get("MinAPIVersion").asText()));
            System.out.println("Git Commit: ".concat(jn.get("GitCommit").asText()));
            System.out.println("Go Version: ".concat(jn.get("GoVersion").asText()));
            System.out.println("OS/Arch: ".concat(jn.get("Os").asText()).concat("/")
                                                            .concat(jn.get("Arch").asText()));
            System.out.println("Kernel Version: ".concat(jn.get("KernelVersion").asText()));
            System.out.println("Build Time: ".concat(jn.get("BuildTime")
                                             .asText().replace("T", " ")
                                            .substring(0, 19)));
        } catch (Exception e) {
            System.out.println("Some information is missing...");
        }
    }
}

