import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> usernames = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
//                if (!usernames.contains(clientUserName)) {
//                    usernames.add(clientUserName);
//                    clientHandlers.add(this);
//                    break;
//                } else {
//                    bufferedWriter.write("Username: " + clientUserName + " is already in use.");
//                    bufferedWriter.newLine();
//                    bufferedWriter.flush();
//                }
            transmitMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String getClientUsername() {
        return clientUserName;
    }

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


    public void removeClientHandler() {
        clientHandlers.remove(this);
        transmitMessage("SERVER: " + clientUserName + " has left server");
    }

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientHandler that = (ClientHandler) object;
        return Objects.equals(clientUserName, that.clientUserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, bufferedReader, bufferedWriter, clientUserName);
    }

}
