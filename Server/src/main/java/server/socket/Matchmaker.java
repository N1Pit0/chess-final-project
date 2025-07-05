package server.socket;

import java.net.Socket;

public interface Matchmaker {
    /**
     * Add a new client socket to the matchmaking queue.
     * When two clients are available, pair them and return a Match.
     * If no pair available yet, return null.
     */
    Match addClient(Socket client);

    /**
     * Represents a matched pair of clients.
     */
    class Match {
        private final Socket player1;
        private final Socket player2;

        public Match(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public Socket getPlayer1() {
            return player1;
        }

        public Socket getPlayer2() {
            return player2;
        }
    }
}