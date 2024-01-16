package graphics;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * AnalyticsPage class represents a page containing a grid with a list of containers,
 * along with associated fields to manage container information.
 *
 * The class includes a grid for the list, an array of container IDs, a list of checkboxes,
 * a list of selected container indices, a grid for container information, 
 * and an array of container names.
 *
 * @see javafx.scene.layout.GridPane
 * @see javafx.scene.control.RadioButton
 * @see java.util.ArrayList
 */
public class AnalyticsPage {
    
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

    /**
     * Default constructor for the AnalyticsPage class.
     */
    public AnalyticsPage() {
    }



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
        monitorAPI.initializeContainerModels(false); //show just running
    
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


    /**
      * Creates a VBox with a list of containers, including a white-background GridPane
      * within a ScrollPane. The list has adjustable scroll bars and is accompanied by
      * a title ("List Of Containers"). Custom styling and padding are applied.
      *
      * @return The VBox containing the list of containers and additional components.
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
        ListPane listPane = new ListPane();
        Text title = listPane.getText("List Of Containers");
        VBox vbox = new VBox();
    
        vbox.getChildren().add(title);
        vbox.setSpacing(20);
        vbox.getChildren().add(listScrollPane);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new javafx.geometry.Insets(0, 50, 300, 50));
        
        return vbox;
    
    }
    
}
