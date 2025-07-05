package server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.controller.ChessGameController;
import server.model.entity.Match;
import server.services.board.BoardService;
import server.services.pgn.PgnService;
import shared.dtos.BoardState;
import shared.dtos.GameInit;
import shared.dtos.GameStatus;
import shared.enums.GameStatusType;
import shared.enums.PieceColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ChessServer {

    private final PgnService pgnService;
    private final ChessGameController gameController;
    private final Matchmaker matchmaker;
    private BoardService boardService;
    private GameStatus gameStatus;


    @Autowired
    public ChessServer(PgnService pgnService, BoardService boardService, ChessGameController gameController, Matchmaker matchmaker) {
        this.pgnService = pgnService;
        this.boardService = boardService;
        this.gameController = gameController;
        this.matchmaker = matchmaker;
    }

    public void handleNewClient(Socket client) {


        Matchmaker.Match match = matchmaker.addClient(client);
        if (match == null) {
            System.out.println("Client waiting for a partner...");
        } else {
            System.out.println("Matched two clients!");
            Socket player1 = match.getPlayer1();
            Socket player2 = match.getPlayer2();
//            this.boardService = new BoardServiceImpl(new Board());
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
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
            e.printStackTrace();
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
        if (gameState != ChessGameController.GameStateEnum.ERROR) {
            if (gameState != ChessGameController.GameStateEnum.ONGOING && gameState != ChessGameController.GameStateEnum.CHECK) {
                Match match = new Match();
                match.setResult("1-0");
                pgnService.save(match);
            }
            setGameStatus(gameState);
        }
    }


    private void startBoardSync(ObjectOutputStream whiteOut, ObjectOutputStream blackOut) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                BoardState boardState = boardService.getBoard().toBoardState();
                boardState.setGameStatus(gameStatus);
                whiteOut.writeObject(boardState);
                blackOut.writeObject(boardState);
            } catch (IOException e) {
                System.out.println("Failed to send board state: " + e.getMessage());
                scheduler.shutdown();  // stop sending if a client disconnects
            }
        }, 0, 50, TimeUnit.MILLISECONDS); // every 1 milliseconds
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

        if (gameStateEnum != ChessGameController.GameStateEnum.ONGOING) {
            this.gameStatus = gameStatus;
        }
    }

}
