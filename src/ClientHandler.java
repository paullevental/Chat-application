import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

// Manages actions between server and client(s)
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    // instantiates ClientHandler class, creates bufferedWriter and reader fields
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
            this.clientUserName = bufferedReader.readLine();
            transmitMessage("SERVER : " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // overwritten run method, to transmit client messages will socket connected to server
    @Override
    public void run() {
        String clientMessage;
        while (socket.isConnected()) {
            try {
                clientMessage = bufferedReader.readLine();
                transmitMessage(clientMessage);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // transmits message for all client Handlers in static ArrayList
    public void transmitMessage(String clientMessage) {
        for (ClientHandler handler : clientHandlers) {
            try {
                if (!handler.clientUserName.equalsIgnoreCase(clientUserName)) {
                    handler.bufferedWriter.write(clientMessage);
                    handler.bufferedWriter.newLine();
                    handler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // Removes client from SERVEr
    public void removeClientHandler() {
        clientHandlers.remove(this);
        transmitMessage("SERVER: " + clientUserName + " has left server");
    }

    // Closes every field associated with client that has disrupted socket
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
