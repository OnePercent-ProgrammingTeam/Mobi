package graphics;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ListPane {
    public GridPane createList() {
        GridPane grid = new GridPane();
        
        grid.setHgap(8);
        grid.setVgap(8);

        //container name
        Label name = new Label("Container Name");
        GridPane.setConstraints(name, 1, 0);

        //container id
        Label id = new Label("Container ID");
        GridPane.setConstraints(id, 2, 0);

        //container status
        Label status = new Label("Status");
        GridPane.setConstraints(status, 3, 0);

        //container time created 
        Label time = new Label("Time Created");
        GridPane.setConstraints(time, 4, 0);

        grid.getChildren().addAll(name, id, status, time);
        return grid;

    }
}
