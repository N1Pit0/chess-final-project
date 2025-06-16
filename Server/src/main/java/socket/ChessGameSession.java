package socket;



import dtos.BoardState;

import java.io.*;
import java.net.Socket;

public class ChessGameSession implements Runnable {
    private final Socket player1;
    private final Socket player2;

    public ChessGameSession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out1 = new ObjectOutputStream(player1.getOutputStream());
                ObjectInputStream in1 = new ObjectInputStream(player1.getInputStream());
                ObjectOutputStream out2 = new ObjectOutputStream(player2.getOutputStream());
                ObjectInputStream in2 = new ObjectInputStream(player2.getInputStream())
        ) {
            while (true) {
                // Player 1 sends a move
                BoardState state1 = (BoardState) in1.readObject();
                out2.writeObject(state1); // Forward to Player 2

                // Player 2 sends a move
                BoardState state2 = (BoardState) in2.readObject();
                out1.writeObject(state2); // Forward to Player 1
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("A player disconnected or an error occurred.");
        }
    }
}
