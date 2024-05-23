import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // starts server Socket
    public void serverStart() {
        try {
            // start server while socket is open
            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);

                // Still needs to be fixed
                Thread thread = new Thread(clientHandler);
                thread.start();

                System.out.println("New client has been connected!!");
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

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            server.serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
