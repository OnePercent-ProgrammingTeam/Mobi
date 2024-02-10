package gr.aueb.dmst.onepercent.programming.cli;

/** The Main class runs the command-line interface (CLI) of the application. */
public class Main {

    /** Default Constructor. */
    public Main() { }
    
    /**
     * The main method of the application.
     * Spawns and starts the menu thread 
     * {@link gr.aueb.dmst.onepercent.programming.cli.MenuThreadCLI}
     * This method serves as the entry point for the CLI application.
     * @param args The command-line arguments passed to the application (not used in this case).
     */
    public static void main(String[] args) {
        MenuThreadCLI menuThreadCLI = new MenuThreadCLI();
        Thread thread = new Thread(menuThreadCLI);
        thread.start();
    }
}
