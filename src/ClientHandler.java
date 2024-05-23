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
            System.out.println("SERVER: " + clientUserName + "has entered the server");
            clientHandlers.add(this);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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
        System.out.println("|==========================|");
        for (ClientHandler handler : clientHandlers) {
            try {
                if (!handler.clientUserName.equalsIgnoreCase(clientUserName)) {
                    handler.bufferedWriter.write(clientMessage);
                    handler.bufferedWriter.newLine();
                    handler.bufferedWriter.flush();
                    System.out.println("|==========================|");
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    public void removeClientHandler() {
        clientHandlers.remove(this);
        transmitMessage("SERVER: " + clientUserName + "has left server");
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
