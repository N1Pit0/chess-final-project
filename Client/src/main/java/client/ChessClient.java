package client;

import dtos.BoardState;
import dtos.GameInit;
import dtos.enums.PieceColor;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChessClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Read initial color
            GameInit gameInit = (GameInit) in.readObject();
            PieceColor myColor = gameInit.getPlayerColor();
            System.out.println("Connected! You are " + myColor);

            Scanner scanner = new Scanner(System.in);
            boolean isMyTurn = myColor == PieceColor.WHITE;

            while (true) {
                if (isMyTurn) {
                    System.out.print("Press Enter to send move...");
                    scanner.nextLine();
//                    BoardState myMove = BoardState.dummy();
                    out.writeObject("myMove");
                    System.out.println("Move sent!");
                }

//                BoardState opponentMove = (BoardState) in.readObject();
//                System.out.println("Received opponent move: " + opponentMove);
                isMyTurn = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection lost: " + e.getMessage());
        }
    }
}
