package gr.aueb.dmst.onepercent.programming;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

public class CSV {
    
    /**Create a csv file with the name of the container, each "column" is:
     * Container Name, Container Id, IP Address, Mac Address, CPU Usage, Date Time
    */
    ContainerMonitorHttp containerMonitorHttp = new ContainerMonitorHttp();
    private String folderPath;
    private static final String[] HEADER = {"Container Name", "Container Id", "IP Address", "Mac Address", "CPU Usage", "Date Time"};

    private void createDataFolder(String userPath) {  //Take for input the folder path given by the user
        
        folderPath = Paths.get(userPath, "Docker Data").toString(); 
        File folder = new File(folderPath); // Create a File object representing the directory
        folder.mkdirs(); // Create the directory and parent directories if they don't exist  
    }

    private void createFile (String filePath) {
        File file = new File(filePath);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))){
            
            /*If file does not exist OR it has no content included in it,
             * then print the name of the columns
             */
            if (!(file.exists() && (file.length() > 0))) { 
                writer.writeNext(HEADER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    boolean exitRequested = false;

    /** Start two threads: 
     * 1) Thread that saves data into a .csv file (timer)
     * 2) Thread that waits for user's input (-1) to terminate the process 
     */
    private void startThreads(String filePath) {
        Thread userInputThread = new Thread(()-> {
            Scanner sc = new Scanner (System.in);
            System.out.print( "Press -1 to terminate the process: ");
            while (true) {
                String userInput = sc.nextLine();
                if ("-1".equals(userInput)) {
                    exitRequested = true;
                    break;
                }
            }
        });
        userInputThread.setName("Input");
        userInputThread.start();

        Timer timer = new Timer(false); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second
            
            @Override
            public void run() { // The task to run, this will be executed every second
                if (exitRequested == false){
                    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath,true))){
                    
                        String[] info = containerMonitorHttp.prepareStoragedData();
                        writer.writeNext(info);    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000);
        
        try {
            userInputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.cancel();
    }


    private void storeRealTimeData() {
        containerMonitorHttp.getContainerStats("CSV");
        String[] info = containerMonitorHttp.prepareStoragedData();
            
        String filePath = Paths.get(folderPath, info[0] +"Data.csv").toString();
        createFile(filePath);
        startThreads(filePath);
    }

    public void startSavingData() {
        String path = Main.handleInput("Enter the path in which you want the data to be stored: ");
        createDataFolder(path); 
        storeRealTimeData();
    }
}

