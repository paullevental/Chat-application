package model;

// Manages actions between server and client(s)
//public class ClientHandler implements Runnable
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private int hashMapKey;

    public ClientHandler(Socket socket, int hashMapKey) {
        try {
            this.socket = socket;
            this.hashMapKey = hashMapKey;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = reader.readLine();
                if (messageFromClient != null) {
                    ClientHandlerManager.getInstance().broadcastMessage(hashMapKey, messageFromClient);
                }
            } catch (IOException e) {
                ClientHandlerManager.getInstance().broadcastMessage(hashMapKey, "A client Disconnected");
                closeEverything(socket, reader, writer);
                break;
            }
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
