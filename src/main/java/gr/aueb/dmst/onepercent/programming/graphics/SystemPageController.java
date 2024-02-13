package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.gui.MonitorGUI;

import javafx.fxml.FXML;

import javafx.scene.text.Text;

/** A UI Controller page, containing all the necessay system and docker version information.*/
public class SystemPageController {

    /** Monitor field foe accesing monitoring functionalidies. */
    final MonitorGUI monitor = new MonitorGUI();
    
    /** The text for the api version. */
    @FXML
    private Text api_version;
    
    /** The text for the build time. */
    @FXML
    private Text build_time;
    
    /** The text for the docker containers. */
    @FXML
    private Text containers;
    
    /** The text for the git commit. */
    @FXML
    private Text git_commit;
    
    /** The text for the go version. */
    @FXML
    private Text go_version;
    
    /** The text for the identification. */
    @FXML
    private Text identification;
    
    /** The text for the count of images. */
    @FXML
    private Text images;
    
    /** The text for the kernel version. */
    @FXML
    private Text kernel_version;
    
    /** The text for the api min version. */
    @FXML
    private Text min_api_version;
    
    /** The text for the node address. */
    @FXML
    private Text node_address;
    
    /** The text for the node id. */
    @FXML
    private Text node_id;
    
    /** The text for the Operating System (OS). */
    @FXML
    private Text operating_system;
    
    /** The text for the OS archive. */
    @FXML
    private Text os_arch;
    
    /** The text for the OS type. */
    @FXML
    private Text os_type;
    
    /** The text for the OS version. */
    @FXML
    private Text os_version;
    
    /** The text for the count of paused containers. */
    @FXML
    private Text paused;
    
    /** The text for the count of running containers. */
    @FXML
    private Text running;
    
    /** The text for the status. */
    @FXML
    private Text status;
    
    /** The text for the count of stopped containers. */
    @FXML
    private Text stopped;
    
    /** The text for the cpus. */
    @FXML
    private Text total_cpus;
    
    /** The text for the total memory. */
    @FXML
    private Text total_memory;
    
    /** The text for the docker version. */
    @FXML
    private Text version;

    /** Default constructor. */
    public SystemPageController() { }
    
    /**
     * Initializes the system page.
     */
    @FXML
    public void initialize() {
        setupSystem();
        setupDocker();
    }

    /** Set ups the UI components that contain information about the system. */
    private void setupSystem() {
        /* send the http request that retrives system info. */
        monitor.systemInfo();
        /* get the response and split it to an array. */
        StringBuilder system_info = monitor.getSystemInfo();
        String[] system_info_array = system_info.toString().split("\n");
        /* initialize the text nodes with the appropriate content. */
        identification.setText(system_info_array[0]);
        operating_system.setText(system_info_array[1]);
        os_type.setText(system_info_array[2]);
        os_version.setText(system_info_array[3]);
        total_memory.setText(system_info_array[4]);
        total_cpus.setText(system_info_array[5]);
        containers.setText(system_info_array[6]);
        images.setText(system_info_array[7]);
        running.setText(system_info_array[8]);
        paused.setText(system_info_array[9]);
        stopped.setText(system_info_array[10]);
        node_id.setText(system_info_array[11]);
        node_address.setText(system_info_array[12]);
        status.setText(system_info_array[13]);
        /* just make it a little bit more beautiful :). */
        String style = (status.getText().equals("active")) ? "-fx-fill: green;" : "-fx-fill: red;";
        status.setStyle(style);
    }

    /** Set ups the UI components that contain information about the docker version. */
    private void setupDocker() {
        /* send the http request that retrives docker info. */
        monitor.dockerVersion();
        /* get the response and split it to an array. */
        StringBuilder docker_info = monitor.getDockerVersion();
        String[] docker_info_array = docker_info.toString().split("\n");
        /* initialize the text nodes with the appropriate content. */
        api_version.setText(docker_info_array[0]);
        min_api_version.setText(docker_info_array[1]);
        git_commit.setText(docker_info_array[2]);
        go_version.setText(docker_info_array[3]);
        os_arch.setText(docker_info_array[4]);
        kernel_version.setText(docker_info_array[5]);
        build_time.setText(docker_info_array[6]);
        version.setText(docker_info_array[7]);
    }
}
