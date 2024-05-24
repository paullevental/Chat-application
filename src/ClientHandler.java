import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
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
            transmitMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    /*
    public void addClientHandler(String clientUsername) {
        ArrayList<String> userNames = new ArrayList<>();
        for (ClientHandler handler : clientHandlers) {
            userNames.add(handler.clientUserName);
        }
        while (true) {
            if (!userNames.contains(clientUsername)) {
                clientHandlers.add(this);
                System.out.println("SERVER: " + clientUsername + " has entered the server");
                break;
            } else {
                System.out.println("Choose a username other than: " + clientUsername);
            }
        }
    }
    */


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



}
