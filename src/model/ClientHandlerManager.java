package model;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandlerManager {

    private static ClientHandlerManager instance = null;
    private static HashMap<Integer, ArrayList<ClientHandler>> clientHandlerHashMap = new HashMap<>();

    private ClientHandlerManager() {}

    public static synchronized ClientHandlerManager getInstance() {
        if (instance == null) {
            instance = new ClientHandlerManager();
        }
        return instance;
    }

    public HashMap<Integer, ArrayList<ClientHandler>> getClientHandlerHashMap() {
        return clientHandlerHashMap;
    }

    public void addClientHandler(int key, ClientHandler handler) {
        ArrayList<ClientHandler> handlers = clientHandlerHashMap.get(key);
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
        clientHandlerHashMap.put(key, handlers);
    }

    public void broadcastMessage(int key, String message) {
        ArrayList<ClientHandler> handlers = clientHandlerHashMap.get(key);
        if (handlers != null) {
            for (ClientHandler handler : handlers) {
                handler.sendMessage(message);
            }
        }
    }
}
