import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
/*We create this class that create a docker client and check the status of 
the container active- inactive something we need in order to check many other methods */


public class TestsHelper {
    private static TestsHelper th;

    private TestsHelper() { 
        createDockerClient();
    }

    public static TestsHelper getInstance() {
        if (th == null) {
            th = new TestsHelper();
        }
        return th;
    }
 
    /*we create this method in order not to inisialize a container id that 
    a user may not have installed in his computer.
    This way the tests that require containerid run in any computer*/
    public  String getTestid() {
        return dc.listContainersCmd().withShowAll(true).exec().get(0).getId();
    }
    /*create a dockerClient for testing */
    protected DockerClient dc;

    private void createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost("tcp://localhost:2375") //daemon host
            .build();
        dc = DockerClientBuilder.getInstance(config).build();
        dc.versionCmd().exec();
    }
    public boolean checkContainerStatus(String id) {
        List<Container> containers;
        containers = dc.listContainersCmd().withShowAll(false).exec();
        for (Container c : containers) {
            if (c.getId().equals(id)) {
                return true; //container is active
            }
        }
        return false;
    }

    //for testing with the list of all containers
    public List<Container> getAllContainers() {
        return dc.listContainersCmd().withShowAll(true).exec();
    }
    public Container getTesContainer() {
        List<Container> containers;
        containers = dc.listContainersCmd().withShowAll(true).exec();
        for (Container c : containers) {
            if (c.getId().equals(getTestid())) {
                return c; 
            }
        }
        return null;

    }

    
}
