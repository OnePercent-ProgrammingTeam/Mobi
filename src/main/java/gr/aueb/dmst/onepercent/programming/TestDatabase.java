package gr.aueb.dmst.onepercent.programming;
public class TestDatabase { 
    public static void main(String[] args) { 
        //IN THE H2DATABASE.JAVA FILE EVERY METHOD HAS AN EXPLANATION OF WHAT IT DOES IN THE BEGGINING, MORE METHODS WILL BE ADDED IN FUTURE VERSIONS
        //FOR NOW YOU CAN SEE THE DATABASE IN ACTION BY COPY-PASTING A CONTAINER ID ( THE COINTAINER HAS TO BE ACTIVE AND RUNNING )
        H2Database h2 = new H2Database();
        h2.createDatabase();
        h2.insertContainersToDatabase();
        h2.getAllContainerInfo();
        System.out.println(" ");
        h2.inserMetricsToDatabase();
        h2.getAllMetrics();
       
    } 
}
