package model;

import java.io.FileWriter;
import java.io.IOException;

public class MainApplication {


    public static void main(String[] args) {
        int serverCount = 8;

        for (int i = 0; i < serverCount; i++) {
            int port = 8000 + i; // Example: ports 8000 to 8007
            Server server = new Server(i, port);
            Thread serverThread = new Thread(server);
            serverThread.start();
        }

        int[] serverCapacities= {0,0,0,0,0,0,0,0};

        try {
            FileWriter writer = new FileWriter("src/serverCapacities.json");
            writer.write(Json.arrayToJson(serverCapacities));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
