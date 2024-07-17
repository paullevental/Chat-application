package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class

public class Server implements Runnable {

    private ServerSocket serverSocket;
    public static int[] serverPorts = {8000,8001,8002,8003,8004,8005,8006,8007};
    public int port;
    public int indexKey;

    public Server(int hashMapKey, int port) {
        this.indexKey = hashMapKey;
        this.port = port;
    }

    // Starts Server | starts a thread with a clientHandler
    @Override
    public void run() {

        try {
            this.serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, indexKey);
                ClientHandlerManager.getInstance().addClientHandler(indexKey, clientHandler);
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
