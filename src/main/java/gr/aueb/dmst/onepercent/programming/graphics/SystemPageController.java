package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/** ok */
public class SystemPageController {
    /** ok */
    public SystemPageController() { }

    @FXML
    private Text api_version;

    @FXML
    private Text build_time;

    @FXML
    private Text containers;

    @FXML
    private Text git_commit;

    @FXML
    private Text go_version;

    @FXML
    private Text identification;

    @FXML
    private Text images;

    @FXML
    private Text kernel_version;

    @FXML
    private Text min_api_version;

    @FXML
    private Text node_address;

    @FXML
    private Text node_id;

    @FXML
    private Text operating_system;

    @FXML
    private Text os_arch;

    @FXML
    private Text os_type;

    @FXML
    private Text os_version;

    @FXML
    private Text paused;

    @FXML
    private Text running;

    @FXML
    private Text status;

    @FXML
    private Text stopped;

    @FXML
    private Text total_cpus;

    @FXML
    private Text total_memory;

    @FXML
    private Text version;

    /**
     * ok
     */
    @FXML
    public void initialize() {
        setupSystem();
        setupDocker();

    }

    final MonitorHttpGUI monitor = new MonitorHttpGUI();

    private void setupSystem() {
        /*send the http request that retrives system info */
        monitor.systemInfo();
        /*get the response and split it to an array */
        StringBuilder system_info = monitor.getSystemInfo();
        String[] system_info_array = system_info.toString().split("\n");
        /*initialize the text nodes with the appropriate content */
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
        /*just make it a little bit more beautiful :) */
        String style = (status.getText().equals("active")) ? "-fx-fill: green;" : "-fx-fill: red;";
        status.setStyle(style);
    }

    private void setupDocker() {
        /*send the http request that retrives docker info */
        monitor.dockerVersion();
        /*get the response and split it to an array */
        StringBuilder docker_info = monitor.getDockerVersion();
        String[] docker_info_array = docker_info.toString().split("\n");
        /*initialize the text nodes with the appropriate content */
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
