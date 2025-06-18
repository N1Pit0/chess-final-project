package server.socket;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleMatchmaker implements Matchmaker {

    private final Queue<Socket> waitingClients = new LinkedList<>();

    @Override
    public synchronized Match addClient(Socket client) {
        if (waitingClients.isEmpty()) {
            waitingClients.add(client);
            return null; // no pair yet
        } else {
            Socket player1 = waitingClients.poll();
            Socket player2 = client;
            return new Match(player1, player2);
        }
    }
}
