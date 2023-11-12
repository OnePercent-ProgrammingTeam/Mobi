public class Monitor implements Runnable {
    private String name;
    private int time;
    private int priority;
    private Thread t;
    private DockerClusterMonitoring dcm;

    public Monitor(String name, int time, int priority, DockerClusterMonitoring dcm) {
        this.name = name;
        this.time = time;
        this.priority = priority;
        this.dcm = dcm;
    }

    public void run() {
        try {
            //dcm.monitoring();
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Thread " + name + " interrupted.");
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, name);
            t.setPriority(priority);
            t.start();
        }
    }
    
}
