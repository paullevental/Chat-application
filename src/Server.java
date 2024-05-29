import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class Server {

    private ServerSocket serverSocket;

    // instantiates Server class, assigns a ServerSocket
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // Starts Server | starts a thread with a clientHandler
    public void serverStart() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
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

    // main method to create server
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9988);
            Server server = new Server(serverSocket);
            server.serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
