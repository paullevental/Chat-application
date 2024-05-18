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
                Thread thread = new Thread(ClientHandler);
                thread.start();

                System.out.println("New client has been connected!!");
            }
        } catch (IOException e) {
            // method for handling input/output exception
            closeServer();
        }
    }

    // closes server, if IO exception caught
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
