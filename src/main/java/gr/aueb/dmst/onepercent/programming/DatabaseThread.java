package gr.aueb.dmst.onepercent.programming;


public class DatabaseThread extends Thread {
    private String cid;
    private int i;
    private static DatabaseAPI db = new DatabaseAPI();
    
    public DatabaseThread(String a) {
        cid = a;
        i = 1;
    }
    
    public void run() {
        while (true) {

            System.out.println(i);
            db.insertMetrics(cid, i);
            i++;
            
            try {
                Thread.sleep(1000); // 1 second pause
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
