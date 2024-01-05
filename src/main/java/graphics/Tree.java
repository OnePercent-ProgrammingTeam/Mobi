package graphics;


import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import gr.aueb.dmst.onepercent.programming.ManagerHttp;

/**
 * Class: Tree is the class that creates the tree menu of the GUI.
 * The tree menu contains the options that the user can choose from.
 * It's located on the left side of the main page of the GUI.
 * @see GUI
 */
public class Tree {

    /** Field: window is the stage of the GUI.*/
    Stage window;
    /** Field: tree is the tree menu of the GUI.*/
    TreeView<String> tree;
    /** Field: manager is the object of the class ManagerHttp.*/
    ManagerHttp manager = new ManagerHttp();

    /** Method: createTree() creates the tree menu of the GUI.
     *  Contains the options that the user can choose from.
     *  By pressing each option, the user can execute the corresponding functionality.
     *  @param listPane: the list of containers.
     *  @return menu: the tree menu of the GUI.
     */
    public VBox createTree(ListPane listPane) {

        TreeItem<String> root;
        TreeItem<String> containers;
        TreeItem<String> images;
        TreeItem<String> analytics;
        
        
        /* Create the root of the tree. */
        root = new TreeItem<>();
        // root.setExpanded(true);  This shows all the tree items from the very beginning.

        /* Create the branches of the tree.*/
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

        /* Create the tree. */
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        
        /* Set the style of the tree. */
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
            
        /* Set the action of each branch of the tree. Implement the code that
         * is executed when each branch is clicked.
         */
        tree.getSelectionModel().selectedItemProperty()
            .addListener((V, oldValue, newValue) -> {
                if (newValue != null && newValue.getValue().equals("Start")) {
                    /* Execute functionality 1 which is start the container. We keep
                     * compatibility with the CLI application.
                     */
                    int answer = 1; 
                    executeFunctionality(listPane, answer);
                }

                if (newValue != null && newValue.getValue().equals("Stop")) {
                    /* Execute functionality 2 which is stop the container. We do this in
                     *  order to keep compatibility with the CLI application.
                    */
                    int aswer = 2;
                    executeFunctionality(listPane, aswer);
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

        /* Create the menu that contains the tree. */
        VBox menu = new VBox();
        menu.getChildren().add(tree);
        menu.setStyle("-fx-background-color: #2A2A72;");

        return menu;
    }

    /** Method: executeFunctionality() executes the functionality that the user
     *  has chosen from the tree menu.
     *  @param listPane: the list of containers.
     *  @param answer: the answer that relates to the functionality of the user.
     */
    private void executeFunctionality(ListPane listPane, int answer) {
        ArrayList<Integer> indices = listPane.getSelectedIndices();
        
        for (int i : indices) {
            ManagerHttp.containerId = ListPane.ids.get(i);
            GUI.menuThread.handleUserInputGUI(answer);
            //manager.startContainerGUI(ListPane.ids.get(i));
            System.out.println("-----------------------------------------------");
            System.out.println(ListPane.ids.get(i));
            System.out.println("-----------------------------------------------");
        }
    }

    /** Method: makeBranch() creates a branch of the tree.
     *  @param title: the title of the branch.
     *  @param parent: the parent of the branch.
     *  @return item: the branch of the tree.
     */
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        /*This shows all the tree items of this specific item without clicking it.
        * item.setExpanded(false);   
        */
        parent.getChildren().add(item);
        return item;
    }
    
}
