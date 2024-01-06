package graphics;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.MonitorHttp;
/** Class: ListPane is the class that creates the list of containers in the main of the GUI.
 * @see GUI
 */
public class ImagePage {
    
    GridPane grid;
    GridPane gridInfo;

    public GridPane getListGrid() {
        
        /* Create and set the grid.*/
        grid = new GridPane();
        grid.setHgap(70);
        grid.setVgap(20);
        grid.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        grid.setPadding(new javafx.geometry.Insets(50, 80, 100, 50));
        
        /* Create the title of the column, containing the names of the containers.*/
        Label nameColumn = new Label("Image Name");
        nameColumn.setStyle("-fx-font-weight: bold;");
        nameColumn.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        nameColumn.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(nameColumn, 0, 0);

        /* Create the title of the column, containing the ids of the containers.*/
        Label idColumn = new Label("Image ID");
        idColumn.setStyle("-fx-font-weight: bold;");
        idColumn.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        idColumn.setPadding(new javafx.geometry.Insets(-30, 0, 0, 0));
        GridPane.setConstraints(idColumn, 1, 0);


        MonitorHttp monitorHttp = new MonitorHttp();
        monitorHttp.getImagesListGUI();
        ArrayList<String> ids = new ArrayList<String>();
        ids = monitorHttp.getFormattedImageIdsList();
        ArrayList<String> names = new ArrayList<String>();
        names = monitorHttp.getFormattedImageNamesList();


        /* Create the labels with the names of the containers.*/
        for (int i = 0; i < names.size(); i++) {
            Label namesLabel = new Label(names.get(i));
            namesLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(namesLabel, 0, i + 1);
            grid.getChildren().add(namesLabel);
        }

        /* Create the labels with the ids of the containers.*/
        for (int i = 0; i < ids.size(); i++) {
            Label id1 = new Label(ids.get(i));
            id1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            GridPane.setConstraints(id1, 1, i + 1);
            grid.getChildren().add(id1);
        }

       
         /* Add the titles of the columns to the grid.*/
        grid.getChildren().addAll(nameColumn, idColumn);
        return grid;
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

