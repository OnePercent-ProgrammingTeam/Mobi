package graphics;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import gr.aueb.dmst.onepercent.programming.core.ManagerHttp;
import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;

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
    ManagerHttpGUI manager = new ManagerHttpGUI();

    SearchBar searchObject = new SearchBar();



    TextField searchField = new TextField();
    ListView<String> autoCompleteListView = new ListView<>();
    static TreeItem<String> old = new TreeItem<>();

    TreeItem<String> root = new TreeItem<>();
    TreeItem<String> containers;
    TreeItem<String> images;
    TreeItem<String> analytics;
    TreeItem<String> help;
    VBox searchBarvbox;
    TextField pullBarTextField;
    static ListPane listPane;

    /** Method: createTree() creates the tree menu of the GUI.
     *  Contains the options that the user can choose from.
     *  By pressing each option, the user can execute the corresponding functionality.
     *  @param listPane: the list of containers.
     *  @return menu: the tree menu of the GUI.
     */
    public VBox createTree(ListPane listPane) {


        
        this.listPane = listPane;
        // root.setExpanded(true);  This shows all the tree items from the very beginning.
        
        /* Create the branches of the tree.*/
        containers = makeBranch("Containers", root);
        containers.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                GUI.window.setScene(GUI.mainScene);
            }
        });

        makeBranch("Start", containers);
        makeBranch("Stop", containers);
        makeBranch("Info", containers); 
        
        images = makeBranch("Images", root);
        images.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                GUI.window.setScene(GUI.imagesScene);
            }
        });

        TreeItem<String> search = makeBranch("Search", images);
        //Create the search bar TreeItem and initially hide it
        TreeItem<String> searchBarItem = new TreeItem<>("Search");
        searchBarvbox = searchObject.createBar();
        searchBarItem.setGraphic(searchBarvbox); // Set the graphic (content) of the TreeItem
        searchBarItem.setExpanded(false); // This shows the search bar only when "Search" is clicked
        search.getChildren().add(searchBarItem); 
        // Add search bar TreeItem as a child of "Search" branch


        //makeBranch("Pull", images);
        TreeItem<String> pull = makeBranch("Pull", images);
        TreeItem<String> pullBarTextItem = new TreeItem<>("Pull");
        pullBarTextField = searchObject.pullBar();
        pullBarTextItem.setGraphic(pullBarTextField);
        pullBarTextItem.setExpanded(false);
        pull.getChildren().add(pullBarTextItem);


        analytics = makeBranch("Analytics", root);
        analytics.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                GUI.window.setScene(GUI.mainScene);
            }
        });
        makeBranch("CPU Usage", analytics);
        makeBranch("CSV", analytics);

        help = makeBranch("Help", root);
        makeBranch("About", help);
        makeBranch("Exit Mobi", help);


        /* Create the tree. */
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.setPrefHeight(900);
        

         // Set a custom cell factory
        tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new CustomTreeCell();
            }
        });


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

        VBox menu = new VBox();
            
        /* Set the action of each branch of the tree. Implement the code that
         * is executed when each branch is clicked.
         */
        tree.getSelectionModel().selectedItemProperty().addListener((V, oldValue, newValue) -> {
            if (newValue != null) {
                handleTreeItemSelected(newValue);
                old = oldValue;
            }
        });

        tree.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            TreeItem<String> selectedItem = tree.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && selectedItem != null) {
                handleTreeItemSelected(selectedItem);
                old = selectedItem; // Update oldValue on double-click
                event.consume(); // Consume the event to prevent it from triggering default behavior
            }
        });
       
        /* Create the menu that contains the tree. */
        menu.getChildren().add(tree);
        menu.setStyle("-fx-background-color: #2A2A72;");
        return menu;
    }


    /** Method: executeFunctionality() executes the functionality that the user
     *  has chosen from the tree menu.
     *  @param listPane: the list of containers.
     *  @param answer: the answer that relates to the functionality of the user.
     */
    private static void executeFunctionality(ListPane listPane, int answer) {
        int index = listPane.getSelectedIndices();
        ManagerHttp.containerId = ListPane.ids.get(index);
        MonitorHttp.containerId = ListPane.ids.get(index);
        GUI.menuThreadGUI.handleUserInputGUI(answer);
        
        if (answer == 6) {
            listPane.setBlankGridPane();
            listPane.updateGridPane();
        }
    }

    class CustomTreeCell extends TreeCell<String> {

        private Button startButton;
        private Button stopButton;
        private Button infoButton;
        private Button CPUButton;
        private Button aboutButton;
        private Button exitButton;;

        CustomTreeCell() {
            startButton = new Button("Start");

            startButton.setStyle("-fx-background-color: #3a3a9d;" + 
                                 "-fx-text-fill: #e2e2f4;" +
                                 "-fx-font-size: 20px;" + 
                                 "-fx-font-weight: bold;");
            stopButton = new Button("Stop");
            stopButton.setStyle("-fx-background-color: #3a3a9d;" + 
                                "-fx-text-fill: #e2e2f4;" +
                                "-fx-font-size: 20px;" + 
                                "-fx-font-weight: bold;");
            infoButton = new Button("Info");
            infoButton.setStyle("-fx-background-color: #3a3a9d;" + 
                                "-fx-text-fill: #e2e2f4;" +
                                "-fx-font-size: 20px;" + 
                                "-fx-font-weight: bold;");

            CPUButton = new Button("CPU Usage");
            CPUButton.setStyle("-fx-background-color: #3a3a9d;" + 
                               "-fx-text-fill: #e2e2f4;" +
                               "-fx-font-size: 20px;" + 
                               "-fx-font-weight: bold;");
            aboutButton = new Button("About");
            aboutButton.setStyle("-fx-background-color: #3a3a9d;" + 
                                 "-fx-text-fill: #e2e2f4;" +
                                 "-fx-font-size: 20px;" + 
                                 "-fx-font-weight: bold;");
            exitButton = new Button("Exit Mobi");
            exitButton.setStyle("-fx-background-color: #3a3a9d;" + 
                                "-fx-text-fill: #e2e2f4;" +
                                "-fx-font-size: 20px;" + 
                                "-fx-font-weight: bold;");

            //CSVButton = new Button("CSV");

            
            startButton.setOnAction(event -> {
                int answer = 1; 
                handleButtonAction("Start", answer);
            });
            stopButton.setOnAction(event -> {
                int answer = 2;
                handleButtonAction("Stop", answer);
            });
            infoButton.setOnAction(event -> {
                int answer = 6;
                handleButtonAction("Info", answer);
            });
            CPUButton.setOnAction(event -> {
                int answer = 5;
                handleButtonAction("CPU Usage", answer);
            });
            aboutButton.setOnAction(event -> {
                System.out.println("Help button pressed");
                GUI.window.setScene(GUI.helpScene);
            });
            exitButton.setOnAction(event -> {
                System.out.println("Exit button pressed");
                GUI gui = new GUI();
                gui.closeProgram();
            });
            //CSVButton.setOnAction(event -> {
                //int answer = 0;
                //handleButtonAction("CSV", answer);
            //});

        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText("");
            } else {
                TreeItem<String> treeItem = (TreeItem<String>) getTreeItem();

                if (treeItem != null && treeItem.getParent() == root || 
                    treeItem.getParent() == images && treeItem.getValue().equals("Search") ||
                    treeItem.getParent() == images && treeItem.getValue().equals("Pull")) {
                    // This is a parent branch, show the text
                    Text text = new Text("Containers");
                    setGraphic(null); // You can also set graphic for parent branches if needed
                    setText(item);

                } else {
                    switch (item) {
                        case "Start":
                            setGraphic(startButton);
                            break;
                        case "Stop":
                            setGraphic(stopButton);
                            break;
                        case "Info":
                            setGraphic(infoButton);
                            break;
                        case "Search":
                            setGraphic(searchBarvbox);
                            break;
                        case "Pull":
                            setGraphic(pullBarTextField);
                            break;
                        case "CPU Usage":
                            setGraphic(CPUButton);
                            break;
                        //case "CSV":
                            //setGraphic(CSVButton);
                            //break;
                        case "About":
                            setGraphic(aboutButton);
                            break;
                        case "Exit Mobi":
                            setGraphic(exitButton);
                            break;
                        default:
                            setGraphic(null);
                            setText(null); // Set text to null or an empty string
                            break;
                    }
                }

            }
        }
        
        private void handleButtonAction(String buttonName, int answer) {
            System.out.println(buttonName + " button pressed");
            Tree.executeFunctionality(Tree.listPane, answer);
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

    private void handleTreeItemSelected(TreeItem<String> selectedItem) {
        System.out.println(selectedItem.getValue() + " clicked");
        if (selectedItem.getValue().equals("Images")) {
            GUI.window.setScene(GUI.imagesScene);
        } else if (selectedItem.getValue().equals("Containers")) {
            GUI.window.setScene(GUI.mainScene);
        } else if (selectedItem.getValue().equals("Analytics")) {
            GUI.window.setScene(GUI.mainScene);
        }
        if (old != null && old.equals(selectedItem)) {
            System.out.println("Double-clicked on " + selectedItem.getValue());
        }
    }

    /** 
     * Default
     */
    public Tree() {

    }

}


    
