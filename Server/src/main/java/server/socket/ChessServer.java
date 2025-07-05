package server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.entity.Match;
import server.services.MatchService;
import server.services.pgn.PgnService;
import shared.dtos.GameStatus;
import shared.dtos.PgnContent;
import shared.enums.GameStatusType;
import shared.dtos.BoardState;
import shared.dtos.GameInit;
import shared.enums.PieceColor;

import server.controller.ChessGameController;
import server.model.board.Board;
import server.services.board.BoardService;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ChessServer {

    private final BoardService boardService;
    private final PgnService pgnService;
    private final ChessGameController gameController;
    private GameStatus gameStatus;
    private final Matchmaker matchmaker;
    private final MatchService matchService;
    private String matchId;

    private final List<Socket> spectators = new CopyOnWriteArrayList<>();
    private final Map<Socket, ObjectOutputStream> spectatorStreams = new ConcurrentHashMap<>();
    private boolean gameStarted = false;

    @Autowired
    public ChessServer(PgnService pgnService, BoardService boardService, ChessGameController gameController, Matchmaker matchmaker, MatchService matchService) {
        this.pgnService = pgnService;
        this.boardService = boardService;
        this.gameController = gameController;
        this.matchmaker = matchmaker;
        this.matchService = matchService;
    }

    public void handleNewClient(Socket client) {
        if (gameStarted) {
            try {
                spectators.add(client);
                spectatorStreams.put(client, new ObjectOutputStream(client.getOutputStream()));
                System.out.println("New spectator connected.");
            } catch (IOException e) {
                System.out.println("Failed to add spectator: " + e.getMessage());
            }
            return;
        }

        Matchmaker.Match match = matchmaker.addClient(client);
        if (match == null) {
            System.out.println("Client waiting for a partner...");
        } else {
            System.out.println("Matched two clients!");
            Socket player1 = match.getPlayer1();
            Socket player2 = match.getPlayer2();
            gameStarted = true;
            startGameSession(player1, player2);
        }
    }

    private void startGameSession(Socket player1, Socket player2) {
        try {
            PieceColor p1Color = Math.random() < 0.5 ? PieceColor.WHITE : PieceColor.BLACK;
            PieceColor p2Color = (p1Color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

            ObjectOutputStream out1 = new ObjectOutputStream(player1.getOutputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(player2.getOutputStream());
            ObjectInputStream in1 = new ObjectInputStream(player1.getInputStream());
            ObjectInputStream in2 = new ObjectInputStream(player2.getInputStream());

            out1.writeObject(new GameInit(p1Color));
            out2.writeObject(new GameInit(p2Color));

            Thread.sleep(1000);

            ObjectInputStream whiteIn = (p1Color == PieceColor.WHITE) ? in1 : in2;
            ObjectOutputStream whiteOut = (p1Color == PieceColor.WHITE) ? out1 : out2;
            ObjectInputStream blackIn = (p1Color == PieceColor.WHITE) ? in2 : in1;
            ObjectOutputStream blackOut = (p1Color == PieceColor.WHITE) ? out2 : out1;

            startBoardSync(whiteOut, blackOut);
            new Thread(() -> handleGame(whiteIn, whiteOut, blackIn, blackOut)).start();

        } catch (IOException | InterruptedException e) {
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
                    if (processMove(move, whiteOut, blackOut)) break;
                } else {
                    System.out.println("Waiting for Black's move...");
                    String move = (String) blackIn.readObject();
                    if (processMove(move, whiteOut, blackOut)) break;
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

    private boolean processMove(String move, ObjectOutputStream whiteOut, ObjectOutputStream blackOut) throws Exception {
        System.out.println("Received move: " + move);
        String[] squares = move.trim().split("\\s+");
        if (squares.length != 2) {
            System.err.println("Invalid move format: " + move);
            return false;
        }

        String from = squares[0];
        String to = squares[1];

        if (!gameController.setPlayablePiece(from)) {
            System.err.println("Invalid piece selected at: " + from);
            return false;
        }

        ChessGameController.GameStateEnum gameState = gameController.makeMove(to);
        System.out.println("Move " + from + " -> " + to + " executed, State: " + gameState);

        if (gameState != ChessGameController.GameStateEnum.ERROR) {
            if (gameState != ChessGameController.GameStateEnum.ONGOING && gameState != ChessGameController.GameStateEnum.CHECK) {
                Match match = new Match();
                match.setResult(gameState == ChessGameController.GameStateEnum.STALEMATE ? "1/2 - 1/2" : boardService.isWhiteTurn() ? "1-0" : "0-1");
                Match savedMatch = pgnService.save(match);
                setGameStatus(gameState);
                matchId = savedMatch.getId();
                getPgnContent();
                return true;
            }
            setGameStatus(gameState);
        }
        return false;
    }

    private void startBoardSync(ObjectOutputStream whiteOut, ObjectOutputStream blackOut) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                BoardState boardState = boardService.getBoard().toBoardState();
                boardState.setGameStatus(gameStatus);

                if (matchId != null) {
                    PgnContent pgnContent = getPgnContent();
                    boardState.setPgnContent(pgnContent);
                }

                whiteOut.writeObject(boardState);
                blackOut.writeObject(boardState);

                for (Map.Entry<Socket, ObjectOutputStream> entry : spectatorStreams.entrySet()) {
                    try {
                        entry.getValue().writeObject(boardState);
                    } catch (IOException e) {
                        System.out.println("Failed to send to spectator: " + e.getMessage());
                        Socket socket = entry.getKey();
                        spectators.remove(socket);
                        spectatorStreams.remove(socket);
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to send board state to players: " + e.getMessage());
                scheduler.shutdown();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void setGameStatus(ChessGameController.GameStateEnum gameStateEnum) throws Exception {
        GameStatusType gameStatusType = ChessGameController.GameStateEnum.toGameStatusType(gameStateEnum);
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

    private PgnContent getPgnContent() {
        PgnContent content = new PgnContent();
        Match match = matchService.findAllMatches().stream()
                .filter(x -> Objects.equals(x.getId(), matchId))
                .findFirst()
                .orElse(null);
        if (match == null) {
            return null;
        }
        content.setGameContent(match.getPgn());
        content.setUser1("jemala");
        content.setUser2("tyemala");
        content.setResult(match.getResult());
        return content;
    }
}
