package Tests;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.ServerSocket;

public class TestServer {

    // ServerManager serverManager;
    ServerSocket socket1;
    ServerSocket socket2;
    ServerSocket socket3;
    ServerSocket socket4;

    @BeforeEach
    public void setUp() {
        try {
            // serverManager = ServerManager.getInstance();
            socket1 = new ServerSocket(((int)(Math.random() * 9999)));
            socket2 = new ServerSocket(((int)(Math.random() * 9999)));
            socket3 = new ServerSocket(((int)(Math.random() * 9999)));
            socket4 = new ServerSocket(((int)(Math.random() * 9999)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @Test
//    public void testConstructor() {
//        serverManager.getServerSockets().add(socket1);
//        serverManager.getServerSockets().add(socket2);
//        assertEquals(2, serverManager.getServerSockets().size());
//        serverManager.getServerSockets().add(socket3);
//        serverManager.getServerSockets().add(socket4);
//        assertEquals(4, serverManager.getServerSockets().size());
//    }



}
