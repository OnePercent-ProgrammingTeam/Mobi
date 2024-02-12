package gr.aueb.dmst.onepercent.programming.core;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI;

import com.fasterxml.jackson.core.JsonProcessingException; 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

/**
 * An abstract class responsible for executing Docker monitoring functionalities via HTTP requests.
 * Extends {@link SuperHttp}.
 * 
 * <p>This is the core monitoring class of the application. It  has two concrete subclasses,
 * which are responsible for implementing specific actions, related to docker objects and docker
 * system monitoring such as container inspection, image searching, listing of docker objects,
 * retrieval of information about docker system, version, swarm and resource usage. 
 * <ul>
 *   <li>{@link gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI} for the CLI version</li>
 *   <li>{@link gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI} for the GUI version</li>
 * </ul>
 * 
 * <p>The logic behind the inheritance is that GUI and CLI versions are built on top of the same
 * core functionality and backend logic, but they have different user interfaces serving different
 * user needs.
 * 
 * <p>Note that the HTTP requests can only be GET, as this class and it's subclasses are responsible
 * for monitoring Docker objects and system in order to retrieve information. 
 * No POST or DELETE requests are made for management purposes, to execute tasks.
 */
public abstract class MonitorHttp extends SuperHttp {

    /** Inspect  container by retrieving information about it */
    public abstract void inspectContainer();

    /** Search for an image, uploaded on Github */
    public abstract void searchImage();

    /** Mapper to read the response. */
    private ObjectMapper mapper;

    /** Json node for reading the response. */
    private JsonNode jsonNode;

    /** Default Constructor. */
    public MonitorHttp() { }

    /**
     * Retrieves statistics, related to container
     * @param identifier a String identifying the operation to be done by retrieving the stats.
     * @return the http response.
     */
    public abstract CloseableHttpResponse getContainerStats(String identifier);

    /** Inspects docker swarm. */
    public void inspectSwarm() {
        String message = "swarm";
        getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + "/" + message);
        executeRequest(message);
    }

    /**
     * Retrieves the response of the Http request that wants send to the docker daemon.
     * @return the http response.
     */
    @Override
    public CloseableHttpResponse getHttpResponse() {
        try {
            CloseableHttpResponse response = MonitorHttp.HTTP_CLIENT.execute(getRequest);
            if (response.getStatusLine().getStatusCode() == 200) {
                conId = containerId;
            }
            return response;
        } catch (IOException e) {
            System.out.println(RED + "Something went wrong while trying to receive the response.." +
                                                                                            RESET);
        } catch (NullPointerException e) {
            System.out.println(RED + "No value was found..." + RESET);
        }
        return null;
    }

    /** 
     * Calculates and retrieves the real time CPU Usage of a running container. 
     * 
     * <p>The formula for calculatinf the resource usage can be found on:
     * <a href="https://docs.docker.com/engine/api/v1.43/#tag/Container/operation/ContainerStats">
     * Docker Engine API v1.43</a>
     * 
     * @param response_builder The builder, from which the response of the request is readden.
     * @throws JsonProcessingException If an error occurs while executing the http request.
     * @return CPU Usage
     */
    public double getCPUusage(StringBuilder response_builder) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(response_builder.toString());
            double  cpu_delta = jsonNode.at("/cpu_stats/cpu_usage/total_usage").asDouble()
                            - jsonNode.at("/precpu_stats/cpu_usage/total_usage").asDouble();
            double system_delta = jsonNode.at("/cpu_stats/system_cpu_usage").asDouble()
                                - jsonNode.at("/precpu_stats/system_cpu_usage").asDouble();
            double number_cpus = jsonNode.at("/cpu_stats/online_cpus").asDouble();
            if (system_delta == 0) {
                //TO DO(Cristian Scobioala-Nicoglu): Make customized Exception
                System.out.println(RED + "\n\nSomething went wrong.\n"
                                 + "Make sure that the container is running" + RESET);
            }
            double cpuUsage = (cpu_delta / system_delta) * number_cpus * 100.0;
            return cpuUsage;
        } catch (NullPointerException e) {
            System.out.println(RED + "The retrieval of CPU Usage unfortunatelly failed." + RESET);
        }
        return 0.0;
    }

    /** 
     * Calculates and retrieves the real time Memory Usage of a running container. 
     * 
     * <p>The formula for calculatinf the resource usage can be found on:
     * <a href="https://docs.docker.com/engine/api/v1.43/#tag/Container/operation/ContainerStats">
     * Docker Engine API v1.43</a>
     * 
     * @param response_buffer The builder, from which the response of the request is readden.
     * @throws JsonProcessingException If an error occurs while executing the http request.
     * @return Memory Usage
     */
    public double getMemoryUsage(StringBuilder response_buffer) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(response_buffer.toString());
            double  used_memory = jsonNode.at("/memory_stats/usage").asDouble()
                            - jsonNode.at("/memory_stats/stats/cache").asDouble();
            double available_memory = jsonNode.at("/memory_stats/limit").asDouble();
            if (available_memory == 0) {
                //TO DO(Cristian Scobioala-Nicoglu): Make customized Exception
                System.out.println(RED + "\n\nSomething went wrong.\n"
                                 + "Make sure that the container is running" + RESET);
            }
            double memory_usage = (used_memory / available_memory) * 100.0;
            return memory_usage;
        } catch (NullPointerException e) {
            System.out.println(RED + "The retrieval of CPU Usage unfortunatelly failed." + RESET);
        }
        return 0.0;
    }

    /**
     * Retrieves information about a container in an array for table representation.
     * 
     * <p> It is used fir inserting data into the tables of the databases and also for
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
     * @throws JsonProcessingException if there is an error processing the JSON response.
     */
    public String[] retrieveContainerInfoArray() throws JsonProcessingException {
        String[] info = new String[8];
        try {
            getRequest = new HttpGet(MonitorHttp.DOCKER_HOST + 
                                    "/containers/" + 
                                    MonitorHttpCLI.containerId + 
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
            System.out.println(RED + "Exception due to null value" + RESET);
        }
        return info;
    }
}
