package graphics;


import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Tree {

    Stage window;
    TreeView<String> tree;

    public VBox createTree() {

        TreeItem<String> root;
        TreeItem<String> containers;
        TreeItem<String> images;
        TreeItem<String> analytics;
        
        
        // Create the Root of the tree
        root = new TreeItem<>();
        // root.setExpanded(true);         This shows all the tree items from the very beginning.

        // Tree item Containers with parent the root
        containers = makeBranch("Containers", root);
        makeBranch("Start", containers);
        makeBranch("Stop", containers);
        makeBranch("Info", containers); 
        

        images = makeBranch("Images", root);
        makeBranch("Search", images);
        makeBranch("Pull", images);

        analytics = makeBranch("Analytics", root);
        makeBranch("CPU Usage", analytics);
        makeBranch("CSV", analytics);

        // Create Tree
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        
        
        tree.setStyle("-fx-background-color: #2A2A72; -fx-text-fill: white;");

        tree.getStyleClass().add("tree-view");
        tree.lookup(".tree-view")
            .setStyle(
                "-fx-control-inner-background: #2A2A72; " + 
                "-fx-padding: 70 0 0 0;" + 
                "-fx-tree-cell-padding: 100px; " + 
                "-fx-tree-cell-border-width: 0 0 1 0; " + 
                "-fx-text-fill: white;" + 
                "-fx-tree-cell-border-color: #2A2A72;" + 
                "-fx-font-weight: bold;" + 
                "-fx-font-size: 20px;");
            
        
        
        
        
        // Implement code to be executed every time a tree item is clicked
        tree.getSelectionModel().selectedItemProperty()
            .addListener((V, oldValue, newValue) -> {
                if (newValue != null && newValue.getValue().equals("Start")) {
                    // Code to be executed when the tree item Start is clicked
                    System.out.println(newValue.getValue());
                }

                if (newValue != null && newValue.getValue().equals("Stop")) {
                    // Code to be executed when the tree item Stop is clicked
                    System.out.println(newValue.getValue()); 
                }

                if (newValue != null && newValue.getValue().equals("Info")) {
                    // Code to be executed when the tree item Info is clicked
                    System.out.println(newValue.getValue()); 
                }

                if (newValue != null && newValue.getValue().equals("Search")) {
                    // Code to be executed when the tree item Search is clicked
                    System.out.println(newValue.getValue()); 
                }

                if (newValue != null && newValue.getValue().equals("Pull")) {
                    // Code to be executed when the tree item Pull is clicked 
                    System.out.println(newValue.getValue());
                } 

                if (newValue != null && newValue.getValue().equals("CPU usage")) {
                    // Code to be executed when the tree item CPU usage is clicked
                    System.out.println(newValue.getValue()); 
                }

                if (newValue != null && newValue.getValue().equals("CSV")) {
                    // Code to be executed when the tree item CSV is clicked
                    System.out.println(newValue.getValue()); 
                }
            });

        VBox menu = new VBox();
        menu.getChildren().add(tree);
        menu.setStyle("-fx-background-color: #2A2A72;");


        return menu;
    }

    //Create Branches for your Tree
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        //This shows all the tree items of this specific item without clicking it.
        // item.setExpanded(false);   
        
        parent.getChildren().add(item);
        return item;
    }
    
}
