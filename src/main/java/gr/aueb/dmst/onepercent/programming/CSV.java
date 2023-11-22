package gr.aueb.dmst.onepercent.programming;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.github.dockerjava.api.model.Container;

public static class CSV {

    private void writeContainerInfoToCsv(List<Container> containers, String filepath) {


       
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            // Write header
            writer.writeNext(new String[]{"ID", "State", "Status", "Network", "Created"});
            
            // Write data
            for (Container container : containers) {
                writer.writeNext(new String[]{
                    container.getId(),
                    container.getState(),
                    container.getStatus(),
                    container.getNetworkSettings().toString(),
                    container.getCreated()
                    }
                );
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
}
