package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class Server implements Runnable {

    private ServerSocket serverSocket;
    public static int[] serverPorts = {1111,2222,3333,4444,5555,6666,7777,8888};
    public int hashMapKey;

    // instantiates Server class, assigns a ServerSocket
    public Server(int hashMapKey) {
        this.hashMapKey = hashMapKey;
    }

    // Starts Server | starts a thread with a clientHandler
    @Override
    public void run() {

        try {
            this.serverSocket = new ServerSocket(serverPorts[hashMapKey]);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, hashMapKey);
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
