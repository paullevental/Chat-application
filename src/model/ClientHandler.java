package model;

// Manages actions between server and client(s)
// public class ClientHandler implements Runnable

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private int indexKey;

    public ClientHandler(Socket socket, int indexKey) {
        try {
            this.socket = socket;
            this.indexKey = indexKey;
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
                System.out.println(messageFromClient);
                if (messageFromClient != null) {
                    ClientHandlerManager.getInstance().broadcastMessage(indexKey, messageFromClient);
                }
            } catch (IOException e) {
                int[] serverCapacities = Json.readJsonFile();
                assert serverCapacities != null;
                serverCapacities[indexKey] -= 1;
                Json.writeToJson(Json.arrayToJson(serverCapacities));
                ClientHandlerManager.getInstance().removeClientHandler(indexKey, "Client has disconnected", this);
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
