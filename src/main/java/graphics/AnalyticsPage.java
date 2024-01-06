package graphics;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AnalyticsPage {
    
    ListPane listPane = new ListPane();
    
    public VBox createList() {
        GridPane grid = listPane.getListGrid("CHOICEBOXES");
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
