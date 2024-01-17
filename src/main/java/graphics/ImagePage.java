package graphics;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;


import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
/** Class: ListPane is the class that creates the list of containers in the main of the GUI.
 * @see GUI
 */
public class ImagePage {
    
    GridPane grid;
    GridPane gridSearch;
    GridPane gridPull;
    HBox hbox;

    /**
     * Method: getListGrid() creates and sets up a GridPane with columns for image names and IDs.
     *
     * @return grid: the GridPane containing the list of images
     */
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


        MonitorHttpGUI monitorHttp = new MonitorHttpGUI();
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
        Text title = listPane.getText("List Of Images");
        VBox vbox = new VBox();

        Text searchText = listPane.getText("Search");
        Text pullText = listPane.getText("Pull");

        HBox hboxTitles = titles(searchText, pullText);

        ScrollPane searchScrollPane = getSearchArea();
        ScrollPane pullScrollPane = getPullArea();

        HBox hboxScroll = searchAndpull(searchScrollPane, pullScrollPane);
    
        vbox.getChildren().add(title);
        vbox.setSpacing(20);
        vbox.getChildren().add(listScrollPane);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new javafx.geometry.Insets(0, 50, 300, 50));
        
        return vbox;
    
    }


    static ScrollPane searchScrollPane;
    /**
     * Creates the field in which the output of the serach will appear
     * @return ScrollPane that will appear in the screen
     */
    public ScrollPane getSearchArea() {

        Text text1 = new Text("Image: 5 \n");
        Text text2 = new Text("everything is fine");
        TextFlow textsearch = new TextFlow(text1, text2);
        VBox vbox = new VBox(textsearch);

        searchScrollPane = new ScrollPane(vbox);
        //searchScrollPane.setPadding(new javafx.geometry.Insets(50, 550, 0, 0));
        

        searchScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        searchScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        searchScrollPane.setMinHeight(200);
        searchScrollPane.setMinWidth(500);
        return searchScrollPane;
        
    }

    public static void setContentSearch(StringBuilder stringBuilder) {
        Text textnew = new Text(stringBuilder.toString());
        TextFlow textflownew = new TextFlow(textnew);

        VBox vboxnew = new VBox(textflownew);
        searchScrollPane.setContent(vboxnew);
    }


    /**
     * Creates the field in which the output of the pull will appear
     * @return ScrollPane that will appear in the screen
     */
    public ScrollPane getPullArea() {
        gridPull = new GridPane();
        gridPull.setHgap(70);
        gridPull.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);

        Label pull = new Label();
        pull.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        GridPane.setConstraints(pull, 0, 0);
        gridPull.getChildren().add(pull);

        gridPull.setStyle("-fx-background-color: #FFFFFF;");
        ScrollPane pullScrollPane = new ScrollPane(gridSearch);
        pullScrollPane.setPadding(new javafx.geometry.Insets(50, 950, 0, 650));
        
        pullScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pullScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pullScrollPane.setMinHeight(200);
        pullScrollPane.setMaxWidth(400);
        return pullScrollPane;

    }


    /**
     * Creates the HBox
     * @param scroll1 the first scrolpane
     * @param scroll2 to do
     * @return the hbox
     */
    public HBox searchAndpull(ScrollPane scroll1, ScrollPane scroll2) {
        hbox = new HBox(200);

        hbox.getChildren().addAll(scroll1, scroll2);
        return hbox;
    }

    /**
     * to do
     * @param text1 to do
     * @param text2 to do
     * @return to do
     */
    public HBox titles(Text text1, Text text2) {
        hbox = new HBox(600);

        hbox.getChildren().addAll(text1, text2);
        return hbox;
    }

    /**
     * Default Constructor
     */
    public ImagePage() {

    }

}
