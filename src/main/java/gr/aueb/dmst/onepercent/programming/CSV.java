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

    /**
     * Create a csv file with the name of the container, each "column" is:
     * Container Name, Container Id, IP Address, Mac Address, CPU Usage
     */

    ContainerMonitorHttp containerMonitorHttp = new ContainerMonitorHttp();
    private String folderPath;
    private static final String[] HEADER = { "Container Name", "Container Id", "IP Address", "Mac Address", "CPU Usage",
            "Date Time" };

    public void createDataFolder(String userPath) { // Take for input the folder path given by the user

        folderPath = Paths.get(userPath, "Docker Data").toString();
        File folder = new File(folderPath);// Create a File object representing the directory
        folder.mkdirs(); // Create the directory and parent directories if they don't exist
    }

    public void createFile(String filePath) {
        File file = new File(filePath);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {

            /*
             * If file does not exist OR it has no content included in it,
             * then print the name of the columns
             */
            if (!(file.exists() && (file.length() > 0))) {
                writer.writeNext(HEADER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getRealTimeData() {
        containerMonitorHttp.getContainerStats();
        String[] info = containerMonitorHttp.prepareStorageData();
        // String filename = info[0] + "Data";
        // String filePath = filename +".csv"; //create a file in the specified path
        System.out.println("OK");
        String filePath = Paths.get(folderPath, info[0] + "Data.csv").toString();
        createFile(filePath);
        Timer timer = new Timer(false); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second

            @Override
            public void run() { // The task to run, this will be executed every second

                try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {

                    String[] info = containerMonitorHttp.prepareStorageData();
                    writer.writeNext(info);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000);

    }

    public static void main(String[] args) {

        CSV csv = new CSV();
        System.out.println("Give your path: ");

        Scanner sc = new Scanner(System.in);
        final String path = sc.nextLine();
        csv.createDataFolder(path);

        csv.getRealTimeData();
    }
}
