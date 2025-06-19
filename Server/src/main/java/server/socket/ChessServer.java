package server.socket;

import dtos.GameStatus;
import enums.GameStatusType;
import server.controller.ChessGameController;
import dtos.BoardState;
import dtos.GameInit;
import enums.PieceColor;
import server.model.board.Board;
import server.services.board.BoardInterface;
import server.services.board.BoardService;
import server.services.board.BoardServiceImpl;
import server.services.checkmatedetection.CheckmateDetectorImpl;

import javax.swing.plaf.synth.Region;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChessServer {

    private final BoardInterface board = new Board();
    private final BoardService boardService = new BoardServiceImpl(board);
    private GameStatus gameStatus;

    private final ChessGameController gameController;

    public ChessServer() {
        gameController = new ChessGameController(boardService, new CheckmateDetectorImpl());
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
                if (boardService.isWhiteTurn()) {
                    System.out.println("Waiting for White's move...");
                    String move = (String) whiteIn.readObject();
                    processMove(move, whiteOut, blackOut);
                } else {
                    System.out.println("Waiting for Black's move...");
                    String move = (String) blackIn.readObject();
                    processMove(move, whiteOut, blackOut);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("A player disconnected or network issue: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error during game handling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processMove(String move, ObjectOutputStream whiteOut, ObjectOutputStream blackOut) throws Exception {
        System.out.println("Received move: " + move);

        String[] squares = move.trim().split("\\s+");
        if (squares.length != 2) {
            System.err.println("Invalid move format: " + move);
            return;
        }

        String from = squares[0];
        String to = squares[1];

        if (!gameController.setPlayablePiece(from)) {
            System.err.println("Invalid piece selected at: " + from);
            return;
        }

        ChessGameController.GameStateEnum gameState = gameController.makeMove(to);
        System.out.println("Move " + from + " -> " + to + " executed, State: " + gameState);
        if (gameState != ChessGameController.GameStateEnum.ERROR)
            setGameStatus(gameState);
    }


    public static void main(String[] args) {
        int port = 9999;
        ChessServer server = new ChessServer(); //წერო დააკომიტე ეს რაც დაწერე
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
                boardState.setGameStatus(gameStatus);
                whiteOut.writeObject(boardState);
                blackOut.writeObject(boardState);
            } catch (IOException e) {
                System.out.println("Failed to send board state: " + e.getMessage());
                scheduler.shutdown();  // stop sending if a client disconnects
            }
        }, 0, 1, TimeUnit.MILLISECONDS); // every 1 milliseconds
    }

    private void setGameStatus(ChessGameController.GameStateEnum gameStateEnum) throws Exception {

        GameStatusType gameStatusType = ChessGameController.GameStateEnum.toGameStatusType(gameStateEnum);

        // Determine affected player (for CHECK) or winner (for CHECKMATE)
        PieceColor affectedColor = null;
        if (gameStateEnum == ChessGameController.GameStateEnum.CHECK) {
            affectedColor = boardService.isWhiteTurn() ? PieceColor.WHITE : PieceColor.BLACK;
        } else if (gameStateEnum == ChessGameController.GameStateEnum.CHECKMATE) {
            affectedColor = boardService.isWhiteTurn() ? PieceColor.BLACK : PieceColor.WHITE;
        }

        GameStatus gameStatus = new GameStatus(gameStatusType, affectedColor);

        if(gameStateEnum != ChessGameController.GameStateEnum.ONGOING){
            this.gameStatus = gameStatus;
        }
    }

}
