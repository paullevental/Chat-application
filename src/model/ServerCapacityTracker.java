package model;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerCapacityTracker {

    private static ServerCapacityTracker instance;
    private AtomicInteger[] serverCapacities;

    private ServerCapacityTracker(int numberOfServers) {
        serverCapacities = new AtomicInteger[numberOfServers];
        for (int i = 0; i < numberOfServers; i++) {
            serverCapacities[i] = new AtomicInteger(0);
        }
    }

    public static synchronized ServerCapacityTracker getInstance(int numberOfServers) {
        if (instance == null) {
            instance = new ServerCapacityTracker(numberOfServers);
        }
        return instance;
    }

    public void incrementCapacity(int serverId) {
        serverCapacities[serverId].incrementAndGet();
        System.out.println(serverId);
    }

    public void decrementCapacity(int serverId) {
        serverCapacities[serverId].decrementAndGet();
    }

    public int getCapacity(int serverId) {
        return serverCapacities[serverId].get();
    }

}
