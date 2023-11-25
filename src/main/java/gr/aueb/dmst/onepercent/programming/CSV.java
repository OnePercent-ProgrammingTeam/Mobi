package gr.aueb.dmst.onepercent.programming;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
public class CSV {
    
    /**Create a csv file with the name of the container, each "column" is:
     * Container Name, Container Id, IP Address, Mac Address, CPU Usage
    */
    public void writeContainerInfoToCsv () {
        
        ContainerMonitorHttp containerMonitorHttp = new ContainerMonitorHttp();
        String[] info = containerMonitorHttp.prepareStorageData();
        info[0] = info[0].substring(1);
        String filename = info[0] + "Data";
        String filePath = filename +".csv"; //create a file in the specified path

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            
            writer.writeNext(new String[]{"Container Name", " Container Id", "IP Address", "Mac Address", "CPU Usage"});  
            
            writer.writeNext(info);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

