package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class Server implements Runnable {

    private ServerSocket serverSocket;
    private ServerCapacityTracker capacityTracker;
    public static int[] serverPorts = {8000,8001,8002,8003,8004,8005,8006,8007};
    public static int[] serverCapacities = {0,0,0,0,0,0,0,0};
    public int port;
    public int hashMapKey;

    public Server(int hashMapKey, int port, ServerCapacityTracker capacityTracker) {
        this.hashMapKey = hashMapKey;
        this.port = port;
        this.capacityTracker = capacityTracker;
    }

    public static void addValueServerCapacities(int index) {
        serverCapacities[index] += 1;
    }

    // Starts Server | starts a thread with a clientHandler
    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(serverPorts[hashMapKey]);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new Client(socket, hashMapKey).getHandler();
                ClientHandlerManager.getInstance().addClientHandler(hashMapKey, clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServer();
        }
    }

    // closes server, if IO exception caught, handles IO Exception
    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
