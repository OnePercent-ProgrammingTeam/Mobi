package gr.aueb.dmst.onepercent.programming.cli;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The CSV class is responsible for creating and managing CSV files to store container data.
 * 
 * <p>It utilizes the OpenCSV library to create CSV files and collects real-time container data,
 * which is then stored in these files. The CSV files are created within a specified folder.
 * 
 * <p>This class was initially designed before incorporating database functionality but is still
 * utilized in the CLI version of the application.
 */
public class CSV {
   
    /** Monitor object that is used to get the data of the containers. */
    MonitorCLI monitor = new MonitorCLI();
    /** A boolean that is used to check if the user has requested to terminate the process. */
    boolean exitRequested = false;
    /** The path of the folder in which the .csv file is going to be created. */
    private String folderPath;
    /** The header of the .csv file. */
    private static final String[] HEADER = {"Container Name", 
                                            "Container Id", 
                                            "IP Address", 
                                            "Mac Address", 
                                            "CPU Usage", 
                                            "Date Time"};

    /** Default constructor for the CSV class. */
    public CSV() { }
    
    /**
     * Starts the process of collecting and storing the data in a .csv file.
     * The method prompts the user to enter the path in which the data is going to be stored.
     */
    public void startProcess() {
        String path = ConsoleUnits.promptForInput(
            "Enter the path in which you want the data to be stored: ");
        createDataFolder(path);
        storeData();
    }

    /**
     * Creates a folder in the path that the user has given.
     * @param userPath is the path that the folder is going to be created.
     */
    private void createDataFolder(String userPath) {  
        folderPath = Paths.get(userPath, "Docker Data").toString(); 
        File folder = new File(folderPath); //Create a File object representing the directory.
        folder.mkdirs(); //Create the directory and parent directories if they don't exist.  
    }

    /**
     * Stores the real time data of the containers in a .csv file.
     * The method gets the data of the containers and stores them in a .csv file.
     */
    private void storeData() {
        monitor.getContainerStats("CSV");
        String[] info = monitor.prepareCsvStorageData();
        String filePath = Paths.get(folderPath, info[0] + "Data.csv").toString();
        createFile(filePath);
        collectData(filePath);
    }
    /**
     * Creates a .csv file in the folder that the user has given.
     * @param filePath is the path that the file is going to be created.
     */
    private void createFile(String filePath) {
        File file = new File(filePath);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            //If the file does not exist or the file is empty, then write the header.
            if (!file.exists() && (file.length()) > 0) { 
                writer.writeNext(HEADER);
            }
        } catch (IOException e) {
            System.out.println(ConsoleUnits.RED + 
                "An error occurred while creating the file." + ConsoleUnits.RESET);
        } 
    }

    /** Collects continuously real time data until termination signal is received. */
    private void collectData(String filePath) {
        Thread userInputThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            System.out.print("Press -1 to terminate the process: ");
            //Wait for user's input to terminate the process.
            while (true) {
                String userInput = sc.nextLine();
                /*
                 *If user wants to terminate the process of saving data,
                 *then set the boolean exitRequested to true and break the loop.
                 */
                if (userInput.equals("-1")) {
                    exitRequested = true;
                    break;
                }
            }
        });
        userInputThread.setName("Input");
        userInputThread.start();
        Timer timer = new Timer(false); // Create a new timer
        /*
         *Schedule a task to run every second
         * This is because the data stored in the .csv file is updated every second - real time.
         */
        timer.scheduleAtFixedRate(new TimerTask() { 
            @Override
            public void run() { // The task to run, this will be executed every second
                if (exitRequested == false) {
                    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
                        String[] info = monitor.prepareCsvStorageData();
                        writer.writeNext(info);    
                    } catch (IOException e) {
                        System.out.println(ConsoleUnits.RED + 
                            "An error occurred while writing to the file." + ConsoleUnits.RESET);
                    }
                }
            }
        }, 0, 1000);
        try {
            userInputThread.join();
        } catch (InterruptedException e) {
            System.out.println(ConsoleUnits.RED + 
                "An error occurred while waiting for your input." + ConsoleUnits.RESET);
        }
        timer.cancel();
    }
}
