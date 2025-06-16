package socket;

import dtos.BoardState;
import dtos.GameInit;
import dtos.enums.PieceColor;
import model.board.Board;
import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessServer {

    private final static BoardInterface board = new Board();
    private final BoardService boardService = new BoardServiceImpl(board);

    private final Matchmaker matchmaker = new SimpleMatchmaker();

    public void handleNewClient(Socket client) {



        Matchmaker.Match match = matchmaker.addClient(client);
        if (match == null) {
            System.out.println("Client waiting for a partner...");
        } else {
            System.out.println("Matched two clients!");
            Socket player1 = match.getPlayer1();
            Socket player2 = match.getPlayer2();
            startGameSession(player1, player2);
        }
    }

    private void startGameSession(Socket player1, Socket player2) {
        try {
            // Assign colors
            PieceColor p1Color = Math.random() < 0.5 ? PieceColor.WHITE : PieceColor.BLACK;
            PieceColor p2Color = (p1Color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

            ObjectOutputStream out1 = new ObjectOutputStream(player1.getOutputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(player2.getOutputStream());
            ObjectInputStream in1 = new ObjectInputStream(player1.getInputStream());
            ObjectInputStream in2 = new ObjectInputStream(player2.getInputStream());

            // Send initial game data
            out1.writeObject(new GameInit(p1Color));
            out2.writeObject(new GameInit(p2Color));

            // White always goes first
            ObjectInputStream whiteIn = (p1Color == PieceColor.WHITE) ? in1 : in2;
            ObjectOutputStream whiteOut = (p1Color == PieceColor.WHITE) ? out1 : out2;
            ObjectInputStream blackIn = (p1Color == PieceColor.WHITE) ? in2 : in1;
            ObjectOutputStream blackOut = (p1Color == PieceColor.WHITE) ? out2 : out1;

            new Thread(() -> handleGame(whiteIn, whiteOut, blackIn, blackOut)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGame(ObjectInputStream whiteIn, ObjectOutputStream whiteOut,
                            ObjectInputStream blackIn, ObjectOutputStream blackOut) {
        try {
            while (true) {
                // White move
                BoardState whiteMove = (BoardState) whiteIn.readObject();
                blackOut.writeObject(whiteMove);

                // Black move
                BoardState blackMove = (BoardState) blackIn.readObject();
                whiteOut.writeObject(blackMove);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("A player disconnected or game ended: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 9999;
        ChessServer server = new ChessServer();
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chess server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                executor.submit(() -> server.handleNewClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
