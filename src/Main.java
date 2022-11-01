public class Main {

    public static void main(String[] args) throws InterruptedException {
        DisplayManager displayManager = new DisplayManager("displayMgrTh");
        StatusManager statusManager = new StatusManager("statusMgrTh");

        displayManager.initialize();
        statusManager.initialize();
    }
}
