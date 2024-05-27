import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String messageToSend) {
        try {
            bufferedWriter.write(username + " : " + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String groupMessage;
                while (socket.isConnected()) {
                    try {
                        groupMessage = bufferedReader.readLine();
                    } catch (IOException e) {
                        closeClient(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

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

    public static void main(String[] args) {
        try {
            String username = JOptionPane.showInputDialog("Enter your username to connect to the messaging system");
            Socket socket = new Socket("localhost", 9988);
            Client client = new Client(socket, username);
            ClientRoom clientRoom = new ClientRoom(client);
            client.listenForMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
