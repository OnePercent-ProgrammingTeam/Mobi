package gr.aueb.dmst.onepercent.programming;

public class DatabaseAPI {
    
    private static H2Database h2 = new H2Database();

    protected void initializeDB() {
        
        h2.createDatabase();
        h2.insertContainersToDatabase();

    }

    protected void getContainerInfo() {

        h2.getAllContainerInfo();

    }

    protected void getMetrics() {
        
        h2.getAllMetrics();

    }

    protected void insertMetrics(String id, int i) {

        h2.insertMetricsToDatabase(id, i);  

    }
}
