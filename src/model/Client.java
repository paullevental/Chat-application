package model;

import java.io.*;
import java.net.Socket;


// Handles actions made by a client/user
public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private ClientRoom clientRoom;

    // instantiates Client class
    public Client() {
        this.clientRoom = new ClientRoom(this);
    }

    public void connectClientToServer(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }

    // sends a message using the socket
    public void sendMessage(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }

    // listens for message
    // creates a thread which listens for message from client
    // closes client if socket isn't connected
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String groupMessage;
                while (socket.isConnected()) {
                    try {
                        groupMessage = bufferedReader.readLine();
                        if (groupMessage != null && !(groupMessage.equals(username))) {
                            clientRoom.appendMessage(groupMessage);
                        }
                    } catch (IOException e) {
                        closeClient(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    // sets clients username
    public void setUsername(String username) {
        this.username = username;
    }

    // Closes every field associated with client that has disrupted socket
    public void closeClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    // main method to create a client
    public static void main(String[] args) {
        new Client();
    }
}