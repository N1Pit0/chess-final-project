package socket;

import controller.ChessGameController;
import controller.GameController;
import dtos.BoardState;
import dtos.GameInit;
import dtos.enums.PieceColor;
import model.board.Board;
import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;
import services.checkmatedetection.CheckmateDetectorImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ChessServer {

    private final BoardInterface board = new Board();
    private final BoardService boardService = new BoardServiceImpl(board);


    private final ChessGameController gameController;

    public ChessServer() {
        gameController = new ChessGameController(board, new CheckmateDetectorImpl());
    }

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


            startBoardSync(whiteOut, blackOut);
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
                String whiteMove = (String) whiteIn.readObject();
//                blackOut.writeObject(whiteMove);
                System.out.println(whiteMove);
                String[] squares = whiteMove.split(" ");
                String from = squares[0];
                String to = squares[1];
                if (gameController.setPlayablePiece(from)) {
                    ChessGameController.GameStateEnum gameState = gameController.makeMove(to);
                    if (gameState == ChessGameController.GameStateEnum.ONGOING) {
                        System.out.println("made a move");
                    }
                }

                boardService.getWhitePieces().forEach(x -> {
                    if (x.getCurrentSquare() != null){
                        System.out.print(x.getCurrentSquare().toAlgebraic() +" ");
                    }
                });
                System.out.println();


////                 Black move
//                var blackMove = blackIn.readObject();
////                whiteOut.writeObject(blackMove);
//                System.out.println(blackMove);
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


    private void startBoardSync(ObjectOutputStream whiteOut, ObjectOutputStream blackOut) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                BoardState boardState = board.toBoardState();
                whiteOut.writeObject(boardState);
                blackOut.writeObject(boardState);
            } catch (IOException e) {
                System.out.println("Failed to send board state: " + e.getMessage());
                scheduler.shutdown();  // stop sending if a client disconnects
            }
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS); // every 1 second
    }

}
