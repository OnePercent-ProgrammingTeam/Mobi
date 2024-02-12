package gr.aueb.dmst.onepercent.programming.cli;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.GREEN;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;

import gr.aueb.dmst.onepercent.programming.core.Monitor;
import gr.aueb.dmst.onepercent.programming.core.SystemController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

/**
 * A monitor class responsible for executing Docker monitoring functionalities.
 * 
 * <p>Extends {@link gr.aueb.dmst.onepercent.programming.core.Monitor}.
 * 
 * <p>Monitoring functionalities provided include:
 * <ul>
 *     <li> Container inspection.</li>
 *     <li> Image searching from Docker Hub.</li>
 *     <li> Listing of locally installed images.</li>
 *     <li> Retrieval of information about the Docker System.</li>
 *     <li> Retrieval of information about the Docker Version.</li>
 *     <li> Retrieval of information about the Docker Swarm.</li>
 *     <li> Retrieval of statistics about a running container.</li>
 * </ul>   
 * 
 * <p>Execution of the monitoring functionalities is done by sending
 * HTTP requests to the 
 * <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API</a>. 
 * 
 * <p>Note that HTTP requests made by this class are GET requests for
 *  retrieval of information. The execution of tasks, which involves POST and DELETE requests,
 *  is implemented in classes related to management of Docker object.
 *  @see gr.aueb.dmst.onepercent.programming.cli.ManagerCLI
 */
public class MonitorCLI extends Monitor {
    
    /** Mapper for reading JSON. */
    private final ObjectMapper mapper = new ObjectMapper();
    /** JSON node, used for reading HTTP responses. */
    private JsonNode jn;
    
    /** Default Constructor. */
    public MonitorCLI() { }

    /** Retrieves information about a container, sending a GET HTTP request to the Docker daemon. */
    @Override
    public void inspectContainer() {
        String path = "json"; // get the container information in json format
        Monitor.containerId = ConsoleUnits.promptForInput(
            "Please type the container ID to get info about the container: ");
        conId = containerId;
        getRequest = new HttpGet(DOCKER_HOST.concat("/containers/")
                                            .concat(Monitor.containerId)
                                            .concat("/").concat(path));                       
        executeRequest(path);
        //check other situations with the state
        printOutput(path);
    }

    /** 
     * Searches for images, uploaded in Docker Hub, by sending an HTTP GET request to the Docker 
     * daemon.
     */
    @Override
    public void searchImage() {
        String path = "/images/search"; // get the container statistics in json format
        imageName = ConsoleUnits.promptForInput(
            "Please type the name of the image you want to search for: ");
        dataBaseThread.setImageName(imageName);
        getRequest = new HttpGet(DOCKER_HOST.concat(path)
                                            .concat("?term=")
                                            .concat(imageName)
                                            .concat("&limit=3"));
        executeRequest(path);
        //check other situations with the state
        dataBaseThread.setState("success");
        printOutput(path);
    }

    /**
     * Lists the images that are installed in the system by sending an HTTP GET request to the 
     * Docker daemon.
     */
    public void listImages() {
        String path = "/images/json";
        getRequest = new HttpGet(DOCKER_HOST.concat(path).concat("?all=true&shares-size=true"));
        executeRequest(path);
    }

    /**
     * Retrieves information about the Docker System by sending an HTTP GET request to the Docker 
     * daemon.
     */
    public void systemInfo() {
        String path = "/info";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
        printOutput(path);
    }

    /**
     * Retrieves information about the Docker Version by sending an HTTP GET request to the Docker 
     * daemon.
     */
    public void dockerVersion() {
        String path = "/version";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
        printOutput(path);
    }

    /** 
     * Executes the HTTP request and reads the response.
     * @param path The path / identifier of the action that is going to be executed.
     */
    @Override
    public void executeRequest(String path) {
        try { 
            this.http_response = Monitor.HTTP_CLIENT.execute(getRequest);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.http_response.getEntity().getContent()));
            String inputLine;
            Monitor.response_builder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response_builder.append(inputLine);
                if (path.equals("stats")) {
                    lastCPUUsage = getCPUusage(response_builder); //print real time CPU Usage
                    //dataBaseThread.setState("success");
                    lastMemoryUsage = getMemoryUsage(response_builder); 
                    this.http_response.close();
                    break;
                }
            }
            reader.close();    
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + 
                "Oops, something went wrong related to the HTTP response." + ConsoleUnits.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleUnits.RED + "Network error might have been occured." 
                + ConsoleUnits.RESET); 
        }
    }

    /**
     * Generates a message based on the HTTP response.
     * @param identifier the message that indicates the action that is going to be executed.
     * @return a message that indicates the status of the HTTP request.
     * Generates a custom message based on the status of the HTTP response, that is returned and
     * then printed to the user, followed by specific info.
     */
    @Override
    public String generateResponseMessage(String identifier) {
        String output;
        switch (http_response.getStatusLine().getStatusCode()) {
            case 200:
                output = GREEN + "Successful request." + RESET + "\n";
                dataBaseThread.setState("success");
                break;
            case 400:
                output = RED + "Unsuccessful request."  + RESET + "\n";
                dataBaseThread.setState("failure");
                break;
            case 404:
                output = RED + "The requested resource was not found." + RESET + "\n";
                dataBaseThread.setState("failure");
                break;
            case 500:
                output = RED + "The server encountered an error." + RESET + "\n";
                dataBaseThread.setState("failure");
                break;
            case 503:
                output = RED + "Node is not part of a swarm." + RESET + "\n";
                dataBaseThread.setState("failure");
                break;
            default:
                output = RED + "An error occurred." + RESET  + "\n";
                dataBaseThread.setState("failure");
                break;
        }
        return output;
    }

    /**
     * Prints the output of the HTTP request. 
     * It is the central method that calls the other methods for printing the information about the
     * Docker Swarm, the container, the search results, the Docker System and the Docker Version.
     * @param message the message that indicates the action that is going to be executed.
     */
    @Override
    public void printOutput(String message) {
        String output;
        output = generateResponseMessage(message);
        System.out.println(output);
        switch (message) {
            case "json":
                printContainerInfo();
                break;
            case "/images/search":
                printSearchResults();
                break;
            case "/info":
                printSystemInfo();
                break;
            case "/version":
                printDockerVersion();
                break;
        }
    }

    /**
     * Formats the response of the HTTP request modifying way the information will be printed about 
     * the Docker Swarm.
     * @return a StringBuilder object with the formatted information about the Docker Swarm.
     */
    public StringBuilder formatSwarmInfo() {
        StringBuilder swarmInfo = new StringBuilder();
        try {
            //create a json object from the response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response_builder.toString());
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
            *The same applies for the 'Manager' node.
            */
            if (!(jn.get("JoinTokens").get("Worker").isNull())) {
                swarmInfo.append("There is at least one worker in the swarm\n");
            } 
            if (!(jn.get("JoinTokens").get("Manager").isNull())) {
                swarmInfo.append("There is at least one manager in the swarm\n");
            }
            return swarmInfo;
        } catch (JsonProcessingException e) {
            System.out.println(ConsoleUnits.RED + 
                "Something went wrong while processing the JSON response." + ConsoleUnits.RESET);
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." +
                               ConsoleUnits.RESET);
        }
        return swarmInfo.append(" ");
    }

    /** 
     * Returns information about the real time consumption of resources for a running container.
     * @param calledby the method that called this method.
     * @return the response of the HTTP request.
     * The CloseableHttpResponse object contains the response of the HTTP request and is used as 
     * input for creating the datapoints of the graphs.
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
        Monitor.containerId = ConsoleUnits.promptForInput(outputMessage);
        conId = containerId;
        getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                 "/containers/" + 
                                 Monitor.containerId + 
                                 "/" + message);                        
        return getHttpResponse();
    }

    /**
     * Prints the information about a container.
     * This is done by reading the JSON response and printing the information.
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
            JsonNode jn = mapper.readTree(response_builder.toString());
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
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." +
                               ConsoleUnits.RESET);
        } catch (JsonProcessingException e) {
            System.out.println(ConsoleUnits.RED + 
                "Something went wrong while processing the JSON response." + ConsoleUnits.RESET);
        }
    }

    /**
     * Prints the the results of the search
     * The results contain:
     * 1. Image Name
     * 2. Description
     * 3. Star Count (the times an image has been shared)
     */
    private void printSearchResults() {
        try {
            jn = mapper.readTree(response_builder.toString()); // could use .body()
            if (jn.isArray()) {
                System.out.println("Top 3 searched results\n");
                for (JsonNode n : jn) {
                    System.out.println("Image name: " + n.get("name") 
                        + "\nDescription: " 
                        + n.get("description") 
                        + "\nStar count: " 
                        + n.get("star_count") + "\n");
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println(ConsoleUnits.RED + 
                "Something went wrong while processing the JSON response." + ConsoleUnits.RESET);
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." + 
                               ConsoleUnits.RESET);
        }
    }

    /** Estimates the number of images that will be displayed, based on the user's input. */
    public void estimateDisplayedImages() {
        /*
         * TO DO (Cristian Scobioala-Nicoglu): 
         * 1. Print the number of the locally installed images. 
         * 2. Validate the user's input. If the number of images the user wants to be displayed is 
         *    greater than the number of images installed in the system, create a customized
         *    exception and print a message to the user. 
         */
        //System.out.println("You have ... images installed in your system.");
        System.out.print("Define the number of images you want to be displayed: ");
    }

    /**
     * Prints the list of images that will be displayed, based on the user's input.
     * @param images the number of images that will be displayed.
     * @throws NullPointerException if there is a null value in the JSON response.
     */
    public void printImagesList(int images) {
        try {
            jn = mapper.readTree(response_builder.toString());
            
            System.out.println("-IMAGES-\n");
            System.out.printf("%-40s %-60s", "Image Name", "Image ID");
            System.out.println(" ");
            
            for (int i = 0; i < images; i++) {
                String name = jn.get(i).get("RepoTags").isEmpty() ? "N/A" : 
                        jn.get(i).get("RepoTags").get(0).asText();
                /*
                 * If the name of the image is too long, then it will be shortened
                 * to 35 characters and "..." will be added at the end. 
                 */
                name = (name.length() > 35) ? name.substring(0, 35).concat("...") : name;

                String id = jn.get(i).get("Id").asText();
                /* 
                 * Just make it a little bit more beautiful  by coloring the sha256 hash,
                 * Fun fact: sha256 is a cryptographic hash function that produces 
                 * a fixed-size (256-bit) string of characters - the id of the image
                 */
                id = id.replace(id.substring(0, 6), 
                    ConsoleUnits.BLUE + id.substring(0, 6) + ConsoleUnits.RESET);
                System.out.printf("\n%-40s %-60s", name, id);
            }
        } catch (JsonProcessingException e) {
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." 
                             + ConsoleUnits.RESET);
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + "Something when wrong." + ConsoleUnits.RESET);
        }
    }

    /**
     * Prints the Docker System information. 
     * This is done by reading the JSON response and printing the information. This method
     * is called by dockerVersion().
     */
    private void printSystemInfo() {
        try {
            jn = mapper.readTree(response_builder.toString());
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                   .concat("SYSTEM").concat(ConsoleUnits.RESET));
            System.out.println("Identification: ".concat(jn.get("ID").asText()));
            System.out.println("Operating System: ".concat(jn.get("OperatingSystem").asText()));
            System.out.println("OSType: ".concat(jn.get("OSType").asText()));
            System.out.println("OS Version: ".concat(jn.get("OSVersion").asText()));
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                   .concat("RESOURCES").concat(ConsoleUnits.RESET));
            System.out.println("Total Memory: ".concat(jn.get("MemTotal").asText()));
            System.out.println("Total CPUs: ".concat(jn.get("NCPU").asText()));
            System.out.println("Total Containers: ".concat(jn.get("Containers").asText()));
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                  .concat("IMAGES").concat(ConsoleUnits.RESET));
            System.out.println("Total Images: ".concat(jn.get("Images").asText()));
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                   .concat("CONTAINERS").concat(ConsoleUnits.RESET));
            System.out.println("Running Containers: ".concat(jn.get("ContainersRunning").asText()));
            System.out.println("Paused Containers: ".concat(jn.get("ContainersPaused").asText()));
            System.out.println("Stopped Containers: ".concat(jn.get("ContainersStopped").asText()));
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                   .concat("SWARM").concat(ConsoleUnits.RESET));
            System.out.println("Node ID: ".concat(jn.get("Swarm").get("NodeID").asText()));
            System.out.println("Node Adress: ".concat(jn.get("Swarm").get("NodeAddr").asText()));
            String color;
            //active is green, inactive is red
            color = jn.get("Swarm").get("LocalNodeState").asText().equals("active") ?
                 ConsoleUnits.GREEN : ConsoleUnits.RED;
            System.out.println("Status: ".concat(color)
                                         .concat(jn.get("Swarm").get("LocalNodeState").asText())
                                         .concat(ConsoleUnits.RESET));
            System.out.println("\n".concat(ConsoleUnits.RED)
                                   .concat("WARNINGS").concat(ConsoleUnits.RESET));
            if (jn.get("Warnings").isArray()) {
                for (JsonNode node : jn.get("Warnings")) {
                    System.out.println(ConsoleUnits.YELLOW.concat(node.asText())
                                                   .concat(ConsoleUnits.RESET));
                }
            } else {
                System.out.println(ConsoleUnits.YELLOW.concat(jn.get("Warnings").asText())
                                         .concat(ConsoleUnits.RESET));
            }
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." 
                             + ConsoleUnits.RESET);
        } catch (JsonProcessingException e) {
            System.out.println("Something went wrong while processing the JSON response.");
        }
    }

    /**
     * Prints the Docker Version information. It is called by the method dockerVersion().
     */
    private void printDockerVersion() {
        try {
            jn = mapper.readTree(response_builder.toString());
            System.out.println("\n".concat(ConsoleUnits.BLUE)
                                   .concat("DOCKER VERSION")
                                   .concat(ConsoleUnits.RESET));
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
        } catch (NullPointerException e) {
            System.out.println(ConsoleUnits.RED + "Some information might be missing..." 
                             + ConsoleUnits.RESET);
        } catch (JsonProcessingException e) {
            System.out.println(ConsoleUnits.RED + 
                "Something went wrong while processing the JSON response." + ConsoleUnits.RESET);
        }
    }

        /** 
     * Prepares data to be stored in a CSV file. 
     * 
     * <p>Retrieves information about a container
     * and its statistics, including container name, ID, IP address, MAC address, CPU usage,
     * and timestamp. 
     * 
     * @return the data that will be stored in the .csv file.
     */
    public String[] prepareCsvStorageData() {
        String[] csv_info;
        try {
            getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                    "/containers/" + 
                                    Monitor.containerId + 
                                    "/json");
            executeRequest("prepare storage");
            jn = mapper.readTree(response_builder.toString());
            csv_info = new String[6];
            /* Insert data to the array. */
            csv_info[0] = jn.at("/Name").asText().substring(1);
            /* Ignore the "/" from the container name. */
            csv_info[1] = jn.at("/Id").asText(); //Container ID
            csv_info[2] = jn
                     .at("/NetworkSettings/Networks/bridge/IPAddress")
                     .asText();
            csv_info[3] = jn
                    .at("/NetworkSettings/Networks/bridge/MacAddress")
                    .asText();
            SystemController.getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                              "/containers/" + 
                                              Monitor.containerId + 
                                              "/stats");
            executeRequest("stats");
            csv_info[4] = String.valueOf(lastCPUUsage);
            LocalDate date = LocalDate.now(); 
            LocalTime time = LocalTime.now();
            //Date and Time in one
            csv_info[5] = date.toString() + " " + time.toString().substring(0, 8); 
            return csv_info;
        } catch (Exception e) {
            System.out.println(RED + "Something went wrong, while preparing data to be stored" 
                                + RESET);
            e.printStackTrace();
            return null;
        }
    }
}

