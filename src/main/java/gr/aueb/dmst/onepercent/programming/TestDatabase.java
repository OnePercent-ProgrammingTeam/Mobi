package gr.aueb.dmst.onepercent.programming;
public class TestDatabase { 
    public static void main(String[] args) { 
        /*IN THE H2DATABASE.JAVA FILE EVERY METHOD HAS AN EXPLANATION OF WHAT IT DOES 
        IN THE BEGGINING, MORE METHODS WILL BE ADDED IN FUTURE VERSIONS
        FOR NOW YOU CAN SEE THE DATABASE IN ACTION BY COPY-PASTING A CONTAINER ID 
        ( THE COINTAINER HAS TO BE ACTIVE AND RUNNING )*/
        DatabaseAPI db = new DatabaseAPI();
        db.initializeDB();
        db.getContainerInfo();
        db.getMetrics();
        System.out.println(" ");
        String id = "a7e83052644fb656db6283215634471484a141d5d5e818e8ce2037638c229f6f";
        DatabaseThread dbt = new DatabaseThread(id);
        dbt.start();
       
       
    } 
}
