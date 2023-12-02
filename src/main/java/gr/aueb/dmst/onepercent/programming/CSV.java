package gr.aueb.dmst.onepercent.programming;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
public class CSV {
    
    /**Create a csv file with the name of the container, each "column" is:
     * Container Name, Container Id, IP Address, Mac Address, CPU Usage
    */

    
    ContainerMonitorHttp containerMonitorHttp = new ContainerMonitorHttp();

    public void createFile (String filePath) {
        

        File file = new File(filePath);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))){
            
            /*If file does not exist OR it has no content included in it,
             * then print the name of the columns
             */
            if (!(file.exists() && (file.length() > 0))) { 
                writer.writeNext(new String[]{"Container Name", "Container Id", "IP Address", "Mac Address", "CPU Usage", "Date Time"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }
    
    public void getRealTimeData() {
        containerMonitorHttp.getContainerStats();
        String[] info = containerMonitorHttp.prepareStorageData();
        info[0] = info[0].substring(1);
        String filename = info[0] + "Data";
        String filePath = filename +".csv"; //create a file in the specified path
        createFile(filePath);
        Timer timer = new Timer(false); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second
            
            @Override
            public void run() { // The task to run, this will be executed every second
                    
                try (CSVWriter writer = new CSVWriter(new FileWriter(filePath,true))){
                    
                    ContainerHttpRequest.getRequest = new HttpGet(ContainerManagerHttp.DOCKER_HOST + "/containers/" + ContainerManagerHttp.containerId + "/stats"  );
                    containerMonitorHttp.executeHttpRequest("stats");
                    info[4] = String.valueOf(ContainerHttpRequest.lastCPUUsage); //CPU Usage
                    LocalDate date = LocalDate.now(); 
                    LocalTime time = LocalTime.now();
                    info[5] = date.toString()+"  "+time.toString().substring(0,8); 
                    writer.writeNext(info);    
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000);

    }
    public static void main(String[] args){
        CSV csv = new CSV();
        
        csv.getRealTimeData();
    }
}

