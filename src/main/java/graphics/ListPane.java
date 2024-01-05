package graphics;
import gr.aueb.dmst.onepercent.programming.MonitorAPI;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;

/** Class: ListPane is the class that creates the list of containers in the main of the GUI.
 * @see GUI
 */
public class ListPane {
    
    /** Field: grid is the grid that contains the list.*/
    GridPane grid;
    /** Field: static ids is the list of ids of the containers.*/
    static ArrayList<String> ids;
    /** Field: checkboxesList is the list of checkboxes of the containers.*/
    ArrayList<CheckBox> checkboxesList = new ArrayList<>();
    /** Field: selectedIndices is the list of the indices of the selected containers.*/
    ArrayList<Integer> selectedIndices = new ArrayList<>();

    /** Method: getGrid() creates the grid that contains the list of containers, with
     *  information about their name, id, status and time created. The returned grid
     *  contains the checkboxes of the containers as well.
     * @return grid: the grid that contains the list of containers.
     */
    public GridPane getGrid() {
        
        /* Create and set the grid.*/
        grid = new GridPane();
        grid.setHgap(70);
        grid.setVgap(20);
        grid.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        grid.setPadding(new javafx.geometry.Insets(70, 100, 0, 0));
        
        /* Create the title of the column, containing the names of the containers.*/
        Label name = new Label("Container Name");
        name.setStyle("-fx-font-weight: bold;");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(name, 1, 0);

        /* Create the title of the column, containing the ids of the containers.*/
        Label id = new Label("Container ID");
        id.setStyle("-fx-font-weight: bold;");
        id.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(id, 2, 0);

        /* Create the title of the column, containing the status of the containers
         * (running or exited).
         */
        Label status = new Label("Status");
        status.setStyle("-fx-font-weight: bold;");
        status.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(status, 3, 0);
        
        /* Create the title of the column, containing the the time the containers
         * were created.
         */
        Label time = new Label("Time Created");
        time.setStyle("-fx-font-weight: bold;");
        time.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(time, 4, 0);

        MonitorAPI monitorAPI = new MonitorAPI();
        MonitorAPI.createDockerClient();
        monitorAPI.initializeContainerModels();

        /* Get the information about the containers.*/
        ArrayList<String> names = monitorAPI.getNameList();
        ids = monitorAPI.getIdList();
        ArrayList<String> statuses = monitorAPI.getStatusList();
        ArrayList<String> times = monitorAPI.getTimeCreatedList();

        /* Create the checkboxes of the containers and add them to a list.*/
        for (int i = 0; i < names.size(); i++) {
            CheckBox checkBox = new CheckBox();
            GridPane.setConstraints(checkBox, 0, i + 1);
            grid.getChildren().add(checkBox);
            GridPane.setHalignment(checkBox, HPos.RIGHT);
            checkboxesList.add(checkBox);
        }

        /* Create the labels with the names of the containers.*/
        for (int i = 0; i < names.size(); i++) {
            Label name1 = new Label(names.get(i));
            name1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(name1, 1, i + 1);
            grid.getChildren().add(name1);
        }

        /* Create the labels with the ids of the containers.*/
        for (int i = 0; i < ids.size(); i++) {
            Label id1 = new Label(ids.get(i));
            id1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(id1, 2, i + 1);
            grid.getChildren().add(id1);
        }

        /* Create the labels with the statuses of the containers.*/
        for (int i = 0; i < statuses.size(); i++) {
            Label status1 = new Label(statuses.get(i));
            status1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(status1, 3, i + 1);
            grid.getChildren().add(status1);
        }

        /* Create the labels with the times the containers were created.*/
        for (int i = 0; i < times.size(); i++) {
            Label time1 = new Label(times.get(i));
            time1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(time1, 4, i + 1);
            grid.getChildren().add(time1);
        }

        /* Add the titles of the columns to the grid.*/
        grid.getChildren().addAll(name, id, status, time);
        return grid;
    }

    /** Method: getTitle() creates the title of the list of containers.
     * @return listOfContainers: the title of the list of containers.
     */
    public Text getTitle() {
        Text listOfContainers = new Text("List of Containers");
        listOfContainers.setFont(Font.font("Tahoma", FontWeight.BOLD, 30)); // book old ..
        listOfContainers.setStyle("-fx-fill: #2A2A72;");
        return listOfContainers;
    }

    /** Method: getSelectedIndices() returns the indices of the selected containers.
     * @return selectedIndices: the indices of the selected containers.
     */
    public ArrayList<Integer> getSelectedIndices() {
        for (CheckBox checkBox : checkboxesList) {
            if (checkBox.isSelected()) {
                int index = checkboxesList.indexOf(checkBox);
                selectedIndices.add(index);
                System.out.println(index);
            }
        }
        return selectedIndices;
    }

    /** Method: createList() creates the list of containers.
     * @return vbox: the list of containers.
     */
    public VBox createList() {
        GridPane grid = getGrid();
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Text title = getTitle();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(title, scrollPane);
        vbox.setAlignment(Pos.TOP_CENTER);
        return vbox;
    
    }
}
