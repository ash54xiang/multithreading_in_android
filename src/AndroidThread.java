public abstract class AndroidThread extends Thread {
    private boolean shutdown;

    public AndroidThread(String threadName) {
        this.setName(threadName);
        this.shutdown = false;
    }

    public AndroidThread(String threadName, int priority) {
        this.setName(threadName);
        this.setPriority(priority); // default priority = 5
        this.shutdown = false;
    }

    // @Override
    // public void start() {
    // System.out.println("Starting thread: " + this.getName());
    // super.start();
    // }

    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public String toString() {
        return "ThreadName: " + this.getName();
    }

    public void initialize() {
        // Add necessary initialization
        System.out.println("Initializing thread: " + this.getName());
        this.start();
    }

    public void shutdown() {
        this.shutdown = true;
        try {
            System.out.println("Exiting thread: " + this.getName());
            this.join();
        } catch (InterruptedException e) {
            System.out.println("Catched interrupted exception..." + e);
        }
    }

    @Override
    public void run() {
        System.out.println("This is thread : " + Thread.currentThread().getName());
        super.run();
    }

}
