package model;

public class MainApplication {

    public static void main(String[] args) {

        int serverCount = 8;
        ServerCapacityTracker tracker = ServerCapacityTracker.getInstance(serverCount);

        for (int i = 0; i < serverCount; i++) {
            int port = 8000 + i; // Example: ports 8000 to 8007
            Server server = new Server(i, port, tracker);
            Thread serverThread = new Thread(server);
            serverThread.start();
        }

    }
}
