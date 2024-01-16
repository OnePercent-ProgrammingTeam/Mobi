import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dockerjava.api.model.Container;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;


public class MonitorApiTest {
    MonitorAPI obj = new MonitorAPI();
    TestsHelper th = TestsHelper.getInstance();

    /* TODO
    @Test
    public void testInitializeContainerModels() {
        obj.createDockerClient();
        obj.initializeContainerModels(true); //initialize containerModels variable in obj
        List<Container> containerArr = th.getAllContainers(); //create a list of all containers
        int m = 0; //matches - the count of similar containers
        for (Container c : containerArr) {
            for (ContainerModel cm : obj.getContainerModels()) {
                if (c.getImageId().equals(cm.getImageId())) {
                    m = m + 1;
                } 
            }
        }
        assertEquals(containerArr.size(), m);
    }
    */

    @Test
    public void testGetNameList() {
        ArrayList<String> arr = obj.getNameList(); //setup
        List<Container> conArr = th.getAllContainers();

        int m = 0; //matches
        for (Container c : conArr) {
            for (String containerNames : arr) {
                //create a string array with each one of each container's names 
                String[] names = containerNames.split(" ");
                boolean allNamesSame = true;

                //check if each and every name of a container from MonitorApi
                //is equal to each and every name of a container from TestsHelper
                for (int i = 0; i < names.length; i++) {
                    if (!names[i].equals(c.getNames()[0].substring(1))) {
                        allNamesSame = false; 
                        //if at least 1 name is not the same, it's not a match
                    }
                }

                if (allNamesSame) {
                    m = m + 1; //+1 match
                }
            }
        }
        assertEquals(arr.size(), m);
    }

    @Test
    public void testGetIdList() {
        ArrayList<String> idList = obj.getIdList();
        List<Container> arr = th.getAllContainers();

        int m = 0;
        for (Container c : arr) {
            for (String id : idList) {
                if (c.getId().equals(id)) {
                    m = m + 1;
                } 
            }
        }
        assertEquals(idList.size(), m);
    }

    @Test
    public void testGetStatusList() {
        ArrayList<String> statusList = obj.getStatusList();
        List<Container> arr = th.getAllContainers();

        int m = 0;
        for (Container c : arr) {
            for (String status : statusList) {
                if (c.getStatus().equals(status)) {
                    m = m + 1;
                } 
            }
        }
        assertEquals(statusList.size(), m);
    }

    public void testgetTimeCreatedList() {
        ArrayList<String> createdList = obj.getTimeCreatedList();
        List<Container> arr = th.getAllContainers();

        int m = 0;
        for (Container c : arr) {
            for (String createdDatetime : createdList) {
                long unixTimestamp = c.getCreated();

                // Convert Unix timestamp to LocalDateTime
                LocalDateTime dateTime = LocalDateTime
                                        .ofInstant(Instant
                                                .ofEpochSecond(unixTimestamp), 
                                                                ZoneId.systemDefault());

                // Format the LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);

                if (createdDatetime.equals(formattedDateTime)) {
                    m = m + 1;
                } 
            }
        }
        
        assertEquals(createdList.size(), m);
    }

    @Test
    void testgetImageIdList() {
        ArrayList<String> imageIdList = obj.getImageIdList();
        List<Container> arr = th.getAllContainers();

        int m = 0;
        for (Container c : arr) {
            for (String imageId : imageIdList) {
                if (c.getStatus().equals(imageId)) {
                    m = m + 1;
                } 
            }
        }
        assertEquals(imageIdList.size(), m);
    
    }

    @Test
    void testgetImageNameList() {
        ArrayList<String> imageList = obj.getImageNameList();
        List<Container> arr = th.getAllContainers();

        int m = 0;
        for (Container c : arr) {
            for (String imageName : imageList) {
                if (c.getStatus().equals(imageName)) {
                    m = m + 1;
                } 
            }
        }
        assertEquals(imageList.size(), m);

     
    }
}


