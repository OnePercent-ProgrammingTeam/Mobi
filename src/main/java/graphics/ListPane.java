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
import javafx.scene.Node;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
public class ListPane {
    
    GridPane grid;
    static ArrayList<String> ids;
    ArrayList<CheckBox> checkboxesList = new ArrayList<>();

    public GridPane getGrid() {
        
        grid = new GridPane();
        grid.setHgap(70);
        grid.setVgap(20);
        grid.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        grid.setPadding(new javafx.geometry.Insets(70, 100, 0, 0));
        
        //container name
        Label name = new Label("Container Name");
        name.setStyle("-fx-font-weight: bold;");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(name, 1, 0);

        //container id
        Label id = new Label("Container ID");
        id.setStyle("-fx-font-weight: bold;");
        id.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(id, 2, 0);

        //container status
        Label status = new Label("Status");
        status.setStyle("-fx-font-weight: bold;");
        status.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(status, 3, 0);
        
        //container time created 
        Label time = new Label("Time Created");
        time.setStyle("-fx-font-weight: bold;");
        time.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(time, 4, 0);

        MonitorAPI monitorAPI = new MonitorAPI();
        monitorAPI.createDockerClient();
        monitorAPI.initializeContainerModels();

        ArrayList<String> names = monitorAPI.getNameList();
        ids = monitorAPI.getIdList();
        ArrayList<String> statuses = monitorAPI.getStatusList();
        ArrayList<String> times = monitorAPI.getTimeCreatedList();

        int count = 0;
        //Create CheckBoxes
        for (int i = 0; i < names.size(); i++) {
            CheckBox checkBox = new CheckBox();
            GridPane.setConstraints(checkBox, 0, i + 1);
            grid.getChildren().add(checkBox);
            GridPane.setHalignment(checkBox, HPos.RIGHT);
            checkboxesList.add(checkBox);
        }

        for (int i = 0; i < names.size(); i++) {
            Label name1 = new Label(names.get(i));
            name1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(name1, 1, i + 1);
            grid.getChildren().add(name1);
        }

        for (int i = 0; i < ids.size(); i++) {
            Label id1 = new Label(ids.get(i));
            id1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(id1, 2, i + 1);
            grid.getChildren().add(id1);
        }

        for (int i = 0; i < statuses.size(); i++) {
            Label status1 = new Label(statuses.get(i));
            status1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(status1, 3, i + 1);
            grid.getChildren().add(status1);
        }

        for (int i = 0; i < times.size(); i++) {
            Label time1 = new Label(times.get(i));
            time1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(time1, 4, i + 1);
            grid.getChildren().add(time1);
        }

        grid.getChildren().addAll(name, id, status, time);
        return grid;
    }

    public Text getTitle() {
        Text listOfContainers = new Text("List of Containers");
        listOfContainers.setFont(Font.font("Tahoma", FontWeight.BOLD, 30)); // book old ..
        listOfContainers.setStyle("-fx-fill: #2A2A72;");
        return listOfContainers;
    }

    ArrayList<Integer> selectedIndices = new ArrayList<>();
    
    public ArrayList<Integer> getSelectedIndices() {
        ArrayList<CheckBox> selectedCheckboxes = new ArrayList<>();
        for (Node node : grid.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedCheckboxes.add(checkBox);
                    int index = selectedCheckboxes.indexOf(checkBox);
                    selectedIndices.add(index);
                    System.out.println(index);
                }
            }
        }
        return selectedIndices;
        //return selectedCheckboxes;
    }
 
    public ArrayList<Integer> getSelectedIndices2() {
        for (CheckBox checkBox : checkboxesList) {
            if (checkBox.isSelected()) {
                int index = checkboxesList.indexOf(checkBox);
                selectedIndices.add(index);
                System.out.println(index);
            }
        }
        return selectedIndices;
    }



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
