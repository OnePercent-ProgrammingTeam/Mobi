package gr.aueb.dmst.onepercent.programming.gui;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.onepercent.programming.core.Monitor;

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
 *  @see gr.aueb.dmst.onepercent.programming.gui.ManagerGUI
 */
public class MonitorGUI extends Monitor {

    /** Counter of the results appearing from search, default is 3. */
    public static int searchResultCount = 3;

    /** Lock for response, used for threading safety. */
    protected static final Object response_lock = new Object();

   /** Lock for response, used for threading safety. */
    protected static final Object cpu_lock = new Object();

    /** Array containg information about containers. */
    String[] containerInfo = new String[4];

    /** Default Constructor. */
    public MonitorGUI() { }

    /**
     * Getter for container information. 
     * @return A array containg container information.
     */
    public String[] getContainerInfo() {
        return containerInfo;
    }
    
    /** Retrieves information about a container, sending a GET HTTP request to the Docker daemon. */
    @Override
    public void inspectContainer() {
        try { //Place the information in a nice order.
            String[] res = this.retrieveContainerInfoArray();
            containerInfo[0] = res[6];
            containerInfo[1] = res[7];
            containerInfo[2] = res[4];
            containerInfo[3] = res[5];
        } catch (NullPointerException e) {
            System.out.print(RED.concat("Something went wrong...").concat(RESET));
        }
    }

    /** Retrieves information about images, sending a GET HTTP request to the Docker daemon. */
    public void inspectImage() {
        String message = "/images/";
        getRequest = new HttpGet(DOCKER_HOST.concat(message).concat(imageName).concat("/json"));
        executeRequest(message);
    }

    /** 
     * Searches for images, uploaded in Docker Hub, by sending an HTTP GET request to the Docker 
     * daemon.
     */
    @Override //XXX: After one search the system collapses, check below for further info
    public void searchImage() {
        String message = "/images/search"; // get the container statistics in json format
        dataBaseThread.setImageName(imageName);
        getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                 message + 
                                 "?term=" + 
                                 imageName + "&limit=" + searchResultCount);
        executeRequest(message);
    }

    /** Lists locally installed images. */
    public void listImages() {
        String message = "/images/json";
        getRequest = new HttpGet(Monitor.DOCKER_HOST + message + 
                                "?boolean=false&shared-size=true");
        executeRequest(message);
    }

    /**
     * Retrieves information about the Docker System by
     * sending an HTTP GET request to the Docker daemon.
     */
    public void systemInfo() {
        String path = "/info";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
    }

    /**
     * Retrieves information about the Docker Version by
     * sending an HTTP GET request to the Docker daemon.
     */
    public void dockerVersion() {
        String path = "/version";
        getRequest = new HttpGet(DOCKER_HOST.concat(path));
        executeRequest(path);
    }

     /**
     * Retrieves information about a container in an array for table representation.
     * 
     * <p> It is used for inserting data into the tables of the databases and also for
     * inspecting a container-printing information about it.
     * 
     * @return An array containing container information:
     *         <ul>
     *             <li>[0] Container ID</li>
     *             <li>[1] Container Name</li>
     *             <li>[2] Container Status</li>
     *             <li>[3] Image ID</li>
     *             <li>[4] Network ID</li>
     *             <li>[5] Gateway</li>
     *             <li>[6] IP Address</li>
     *             <li>[7] Mac Address</li>
     *         </ul>
     */
    @Override
    public String[] retrieveContainerInfoArray() {
        String[] info = new String[8];
        try {
            getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                    "/containers/" + 
                                    Monitor.containerId + 
                                    "/json");
            executeRequest("prepare storage");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response_builder.toString());
            info[0] = jsonNode.get("Id").asText();
            info[1] = jsonNode.get("Name").asText().substring(1);
            info[2] = jsonNode.get("State").get("Status").asText();
            info[3] = jsonNode.get("Image").asText();
            info[4] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("NetworkID")
                            .asText();
            info[5] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("Gateway")
                            .asText();
            info[6] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("IPAddress")
                            .asText();
            info[7] = jsonNode.get("NetworkSettings")
                            .get("Networks")
                            .get("bridge")
                            .get("MacAddress")
                            .asText();
        } catch (NullPointerException e) {
            System.out.println(RED + "Exception due to null value." + RESET);
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Some information might be missing." + RESET);
        }
        return info;
    }
   
    @Override
    public CloseableHttpResponse getContainerStats(String message) {
        message = "stats"; // get the container statistics in json format
        conId = containerId;
        getRequest = new HttpGet(Monitor.DOCKER_HOST + 
                                 "/containers/" + 
                                 Monitor.containerId + 
                                 "/" + message);                        
        return getHttpResponse();
    }

    /** 
     * Executes the HTTP request and reads the response.
     * @param path The path / identifier of the action that is going to be executed.
     */
    @Override
    public void executeRequest(String path) {      
        try {
            http_response = HTTP_CLIENT.execute(getRequest);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(http_response.getEntity().getContent()));
            String inputLine;
            //Thread security.
            synchronized (response_lock) {
                response_builder = new StringBuilder();
            }
            while ((inputLine = reader.readLine()) != null) {
                response_builder.append(inputLine);
                if (path.equals("stats")) {
                    synchronized (cpu_lock) {
                        lastCPUUsage = getCPUusage(response_builder);
                    }
                    http_response.close();
                    break;
                }
            } //Close resources.
            reader.close();
            http_response.close();
        } catch (Exception e) {
            System.err.println(RED + "Something's wrong.." + RESET);
        }
    }

    /*
     * XXX: Something's wrong with the search... After searching a image, results appear and
     * then the program doesn't respond. Might be caused by threading issues or problem related to
     * HTTP requests-responses.
     */
    
    /**
     * Returns the result of the search.
     * @param string The autocompleted text.
     * @return A string builder with the result of the search.
     */
    public StringBuilder getSearchResult(String string) {
        StringBuilder searchResult = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response_builder.toString()); // could use .body()
            if (jsonNode.isArray()) {
                for (JsonNode el : jsonNode) {
                    searchResult.append(el.get("name") + "\n" +
                        el.get("description") + "\n" + 
                        el.get("star_count") + "\n");
                }
            }            
        } catch (NumberFormatException e) {
            System.out.println(e.getClass());
        } catch (JsonProcessingException e) {
            System.out.println(e.getClass());
        } catch (Exception e) {
            System.out.println(e.getClass());
        }
        return searchResult;
    }

    /**
     * Returns a list with the formatted images' ids.
     * @return An array list, containing the images ids.
     */
    public ArrayList<String> getFormattedImageIdsList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response_builder.toString()); 
            ArrayList<String> ids = new ArrayList<String>();
            if (jsonNode.isArray()) {
                for (JsonNode nd : jsonNode) {
                    ids.add(nd.get("Id").asText());
                }
            }
            return ids;
        } catch (NullPointerException e) {
            System.out.println(RED + "Oops, something went wrong..." + RESET);
            return null;
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Information might be missing..." + RESET);
            return null;
        }
    }

    /**
     * Returns a list with the formatted images' names.
     * @return An array list, containing the images names.
     */
    public ArrayList<String> getFormattedImageNamesList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response_builder.toString());     
            ArrayList<String> names = new ArrayList<String>();
            if (jsonNode.isArray()) {
                for (JsonNode jn : jsonNode) {
                    if (jn.get("RepoTags").size() != 0) {
                        names.add(jn.get("RepoTags").get(0).asText());
                    }
                }
            }
            return names;
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Something went wrong..." + RESET);
            System.out.println(e.getClass());
            return null;
        }
    }
    
    /**
     * Formats and returns the information about Docker Swarm.
     * @return A String array containg the information about Docker Swarm.
     */
    public String[] getSwarmInfo() {
        String[] swarmInfo = new String[6];
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response_builder.toString());
            swarmInfo[0] = jsonNode.get("Spec").get("Name").asText();
            swarmInfo[1] = jsonNode.get("ID").asText();
            swarmInfo[2] = jsonNode.get("Version").get("Index").asText();
            swarmInfo[3] = jsonNode.get("CreatedAt")
                                    .asText()
                                    .replace("T", "\n")
                                    .substring(0, 19);
            swarmInfo[4] =  jsonNode
                            .get("UpdatedAt")
                            .asText()
                            .replace("T", "\n")
                            .substring(0, 19);
            swarmInfo[5] =  jsonNode.get("SubnetSize").asText();
            return swarmInfo;
        } catch (Exception e) {
            System.out.println(RED + "Some information is missing..." + RESET);
        }
        return null;
    }

    /**
     * Formats and returns the information about the Docker System.
     * @return A String array containg the information about Docker System.
     */
    public StringBuilder getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response_builder.toString());
            sb.append(jn.get("ID").asText().concat("\n"));
            sb.append(jn.get("OperatingSystem").asText().concat("\n"));
            sb.append(jn.get("OSType").asText().concat("\n"));
            sb.append(jn.get("OSVersion").asText().concat("\n"));
            sb.append(jn.get("MemTotal").asText().concat("\n"));
            sb.append(jn.get("NCPU").asText().concat("\n"));
            sb.append(jn.get("Containers").asText().concat("\n"));
            sb.append(jn.get("Images").asText().concat("\n"));
            sb.append(jn.get("ContainersRunning").asText().concat("\n"));
            sb.append(jn.get("ContainersPaused").asText().concat("\n"));
            sb.append(jn.get("ContainersStopped").asText().concat("\n"));
            sb.append(jn.get("Swarm").get("NodeID").asText().concat("\n"));
            sb.append(jn.get("Swarm").get("NodeAddr").asText().concat("\n"));
            sb.append(jn.get("Swarm").get("LocalNodeState").asText().concat("\n"));
            return sb;
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Some information is missing..." + RESET);
        } catch (NullPointerException e) {
            System.out.println(RED + "Something was null" + RESET);
        }
        return sb.append(" ");
    }

    /**
     * Formats the Docker Version information.
     * @return A string Builder containing the information about docker version.
     */
    public StringBuilder getDockerVersion() {
        StringBuilder sb = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response_builder.toString());
            sb.append(jn.get("Version").asText().concat("\n"));
            sb.append(jn.get("ApiVersion").asText().concat("\n"));
            sb.append(jn.get("MinAPIVersion").asText().concat("\n"));
            sb.append(jn.get("GitCommit").asText().concat("\n"));
            sb.append(jn.get("GoVersion").asText().concat("\n"));
            sb.append(jn.get("Os")
                          .asText()
                          .concat("/")
                          .concat(
                                jn.get("Arch").asText()
                          ).concat("\n")
                      );
            sb.append(jn.get("KernelVersion").asText().concat("\n"));
            sb.append(jn.get("BuildTime")
                          .asText()
                          .replace("T", " ")
                          .substring(0, 19)
                          .concat("\n")
                      );
            return sb;
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Some information is missing..." + RESET);
        } catch (NullPointerException e) {
            System.out.println(RED + "Something went wrong." + RESET);
        }
        return sb.append(" ");
    }

    /**
     * Retrieves and returns the system warnings that will be converted into notifications in UI.
     * @return A string builder with the warnings / notifications.
     */
    public StringBuilder getWarnings() {
        StringBuilder sb = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jn = mapper.readTree(response_builder.toString());
            //check that there are warnings.    
            if (!(jn.get("Warnings").isEmpty()) && jn.get("Warnings").isArray()) {
                for (JsonNode node : jn.get("Warnings")) {
                    sb.append(node.asText().concat("\n"));
                }
            } else {
                sb.append("No warnings found");
            }
            return sb;
        } catch (JsonProcessingException e) {
            System.out.println(RED + "Some information is missing..." + RESET);
        } catch (NullPointerException e) {
            System.out.println(RED + "Something was null" + RESET);
        }
        return sb.append(" ");
    }
}
