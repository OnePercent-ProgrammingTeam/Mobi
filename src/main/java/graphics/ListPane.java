package graphics;
import gr.aueb.dmst.onepercent.programming.MonitorAPI;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import gr.aueb.dmst.onepercent.programming.MonitorHttpGUI;
import javafx.scene.control.ToggleGroup;

/** Class: ListPane is the class that creates the list of containers in the main of the GUI.
 * @see GUI
 */
public class ListPane {
    
    /** Field: grid is the grid that contains the list.*/
    GridPane grid;
    /** Field: static ids is the list of ids of the containers.*/
    static ArrayList<String> ids = new ArrayList<>();
    /** Field: checkboxesList is the list of checkboxes of the containers.*/
    ArrayList<RadioButton> radioButtons = new ArrayList<>();
    /** Field: selectedIndices is the list of the indices of the selected containers.*/
    ArrayList<Integer> selectedIndices = new ArrayList<>();
    /** Field: gridInfo is the grid that contains the information about the containers.*/
    GridPane gridInfo;
    static ArrayList<String> names = new ArrayList<>();

    /** Method: getGrid() creates the grid that contains the list of containers, with
     *  information about their name, id, status and time created. The returned grid
     *  contains the checkboxes of the containers as well.
     * @return grid: the grid that contains the list of containers.
     */
    public GridPane getListGrid() {
        
        /* Create and set the grid.*/
        grid = new GridPane();
        grid.setHgap(70);
        grid.setVgap(20);
        grid.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        grid.setPadding(new javafx.geometry.Insets(50, 80, 100, 50));
        
        /* Create the title of the column, containing the names of the containers.*/
        Label name = new Label("Container Name");
        name.setStyle("-fx-font-weight: bold;");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        name.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(name, 1, 0);

        /* Create the title of the column, containing the ids of the containers.*/
        Label id = new Label("Container ID");
        id.setStyle("-fx-font-weight: bold;");
        id.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        id.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(id, 2, 0);

        /* Create the title of the column, containing the status of the containers
         * (running or exited).
         */
        Label status = new Label("Status");
        status.setStyle("-fx-font-weight: bold;");
        status.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        status.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(status, 3, 0);
        
        /* Create the title of the column, containing the the time the containers
         * were created.
         */
        Label time = new Label("Time Created");
        time.setStyle("-fx-font-weight: bold;");
        time.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        time.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(time, 4, 0);

        MonitorAPI monitorAPI = new MonitorAPI();
        MonitorAPI.createDockerClient();
        monitorAPI.initializeContainerModels(true);
    
        /* Get the information about the containers.*/
        names = monitorAPI.getNameList();
        ids = monitorAPI.getIdList();
        ArrayList<String> statuses = monitorAPI.getStatusList();
        ArrayList<String> times = monitorAPI.getTimeCreatedList();

        /* Create the checkboxes of the containers and add them to a list.*/
        ToggleGroup group = new ToggleGroup();
        for (int i = 0; i < ids.size(); i++) {
            RadioButton radioButton = new RadioButton();
            group.getToggles().add(radioButton);
            GridPane.setConstraints(radioButton, 0, i + 1);
            grid.getChildren().add(radioButton);
            GridPane.setHalignment(radioButton, HPos.RIGHT);
            radioButtons.add(radioButton);
        
        }
        /* Create the labels with the names of the containers.*/
        for (int j = 0; j < names.size(); j++) {
            Label name1 = new Label(names.get(j));
            name1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(name1, 1, j + 1);
            grid.getChildren().add(name1);
        }

        /* Create the labels with the ids of the containers.*/
        for (int k = 0; k < ids.size(); k++) {
            Label id1 = new Label(ids.get(k));
            id1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(id1, 2, k + 1);
            grid.getChildren().add(id1);
        }

        /* Create the labels with the statuses of the containers.*/
        for (int n = 0; n < statuses.size(); n++) {
            Label status1 = new Label(statuses.get(n));
            status1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(status1, 3, n + 1);
            grid.getChildren().add(status1);
        }

        /* Create the labels with the times the containers were created.*/
        for (int l = 0; l < times.size(); l++) {
            Label time1 = new Label(times.get(l));
            time1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(time1, 4, l + 1);
            grid.getChildren().add(time1);
        }

        /* Add the titles of the columns to the grid.*/
        grid.getChildren().addAll(name, id, status, time);
        return grid;
    }





    /** Method: getTitle() creates the title of the list of containers.
     * @return listOfContainers: the title of the list of containers.
     */
    public Text getText(String text) {
        Text listOfContainers = new Text(text);
        listOfContainers.setFont(Font.font("Tahoma", FontWeight.BOLD, 20)); // book old ..
        //listOfContainers.setStyle("-fx-fill: #2A2A72;");
        return listOfContainers;
    }

    
    /** Method: getSelectedIndices() returns the indices of the selected containers.
     * @return selectedIndices: the indices of the selected containers.
     */
    public int getSelectedIndices() {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isSelected()) {
                int index = radioButtons.indexOf(radioButton);
                System.out.println(index);
                return index;
            }
        }
        System.out.println("No radio button selected");
        return -1;
    }

    /** Method: createList() creates the list of containers.
     * @return vbox: the list of containers.
     */
    public VBox createList() {
       
        GridPane grid = getListGrid();
        grid.setStyle("-fx-background-color: #FFFFFF;");
        ScrollPane listScrollPane = new ScrollPane(grid);
        
        listScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        listScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        listScrollPane.setMinHeight(500);
        
       /*  scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        */
        Text title = getText("List of Containers");
        Text info = getText("Information");
        VBox vbox = new VBox();
        GridPane infoGrid = getInfoGrid();
       
        infoGrid.setStyle("-fx-background-color: #FFFFFF;");
        ScrollPane infoScrollPane = new ScrollPane(infoGrid);
        //scrollPane2.setPadding(new javafx.geometry.Insets(50, 0, 0, 0));
        
        infoScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        infoScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        infoScrollPane.setMinHeight(200);
        infoScrollPane.setMaxWidth(400);

        vbox.getChildren().add(title);
        vbox.setSpacing(20);
        vbox.getChildren().add(listScrollPane);
        vbox.setSpacing(20);
        vbox.getChildren().add(info);
        vbox.setSpacing(10);
        vbox.getChildren().add(infoScrollPane);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new javafx.geometry.Insets(0, 50, 300, 50));
        
        return vbox;
    
    }

    
    public GridPane getInfoGrid() {
        /* Create and set the grid.*/
        gridInfo = new GridPane();
        gridInfo.setHgap(70);
        //grid.setVgap(10);
        gridInfo.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
       
        /* Create the title of the column, containing the names of the containers.*/
        Label name = new Label("Container Name:");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(name, 0, 0);
        
        Label id = new Label("Container ID:");
        id.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(id, 0, 1);
        
        Label status = new Label("Status:");
        status.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(status, 0, 2);

        Label imageId = new Label("Image ID:");
        imageId.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(imageId, 0, 3);

        Label networkId = new Label("Network ID:");
        networkId.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(networkId, 0, 4);

        Label gateway = new Label("Gateway:");
        gateway.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(gateway, 0, 5);

        Label ipAddress = new Label("IP Address:");
        ipAddress.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(ipAddress, 0, 6);

        Label macAddress = new Label("MAC Address:");
        macAddress.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(macAddress, 0, 7);
        
        gridInfo.getChildren().addAll(name, 
                                  id, 
                                  status, 
                                  imageId, 
                                  networkId, 
                                  gateway, 
                                  ipAddress, 
                                  macAddress);
        return gridInfo;
    }

    String[] containerInfos = new String[8];
    public void updateGridPane() {
        MonitorHttpGUI monitorHttp = new MonitorHttpGUI();
        containerInfos = monitorHttp.getContainerInfoForGUI();

        
        for (int i = 0; i < containerInfos.length; i++) {
            Label containerInfo = new Label(containerInfos[i]);
            containerInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(containerInfo, 1, i);
            gridInfo.getChildren().add(containerInfo);
        }
        
    }

    public void setBlankGridPane() {
        
        for (int i = 0; i < containerInfos.length; i++) {
            Label containerInfo = new Label(containerInfos[i]);
            containerInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            containerInfo.setStyle("-fx-text-fill: #FFFFFF;");
            GridPane.setConstraints(containerInfo, 1, i);
            gridInfo.getChildren().add(containerInfo);
        }
    }


}
