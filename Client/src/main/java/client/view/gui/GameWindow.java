package client.view.gui;

import client.ServerConnection;
import server.model.board.Board;
import server.services.board.BoardInterface;
import server.services.utils.Clock;
import client.view.BoardStateView;
import client.view.mouseListener.BoardMouseListener;
import client.view.mouseListener.CustomMouseListenerImpl;
import shared.dtos.BoardState;
import shared.dtos.PgnContent;
import shared.dtos.PieceState;
import shared.dtos.SquareState;
import shared.enums.PieceColor;

import static shared.enums.ImagePath.RESOURCES_WPAWN_PNG;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameWindow {
    private final JFrame gameWindow;
    private ServerConnection serverConnection;
    private BoardStateView boardView;
    private Clock blackClock;
    private Clock whiteClock;
    private Timer timer;
    private boolean isWhiteTurn = true;
    private JLabel playerColorLabel;

    private String whiteName;
    private String blackName;

    public GameWindow(String blackName, String whiteName, int hh, int mm, int ss) {
        blackClock = new Clock(hh, ss, mm);
        whiteClock = new Clock(hh, ss, mm);

        gameWindow = new JFrame("Chess");

        this.blackName = blackName;
        this.whiteName = whiteName;

        try {
            Image whiteImg = ImageIO.read(getClass().getResource(RESOURCES_WPAWN_PNG.label));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("Game file wp.png not found");
        }

        gameWindow.setLayout(new BorderLayout(20, 20));
        gameWindow.setLocationRelativeTo(null);

        JPanel gameData = gameDataPanel(blackName, whiteName, hh, mm, ss);
        gameWindow.add(gameData, BorderLayout.NORTH);

        BoardInterface board = new Board();
        BoardState boardState = board.toBoardState();

        List<PieceState> pieceStates = new LinkedList<>();
        pieceStates.addAll(boardState.getWhitePieces());
        pieceStates.addAll(boardState.getBlackPieces());

        setPiecesToTheSquares(pieceStates, boardState.getBoardSquareArray());

        boardView = new BoardStateView(boardState);
        gameWindow.add(boardView, BorderLayout.CENTER);

        gameWindow.add(buttons(), BorderLayout.SOUTH);

        gameWindow.pack();
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        startServerConnection();
    }

    private void startServerConnection() {
        serverConnection = new ServerConnection("localhost", 9999, newBoardState -> {
            SwingUtilities.invokeLater(() -> {
                if (newBoardState == null) return;
                List<PieceState> pieces = new LinkedList<>();
                pieces.addAll(newBoardState.getWhitePieces());
                pieces.addAll(newBoardState.getBlackPieces());
                setPiecesToTheSquares(pieces, newBoardState.getBoardSquareArray());

                boardView.updateBoardState(newBoardState);
                isWhiteTurn = newBoardState.isWhiteTurn();
            });
        }, status -> {
            if (status == null) return;
            SwingUtilities.invokeLater(() -> {
                switch (status.statusType()) {
//                    case CHECK ->
//                            JOptionPane.showMessageDialog(null, status.affectedPlayer() + " is in check!", "Check", JOptionPane.WARNING_MESSAGE);
                    case CHECKMATE -> checkmateOccurred(status.affectedPlayer());
                    case STALEMATE -> stalemateOccurred();
                }
            });
        }, gameInit -> {
            SwingUtilities.invokeLater(() -> {
                String colorText = (gameInit.getPlayerColor() == PieceColor.BLACK ? "Black" : "White");
                playerColorLabel.setText("You are playing as: " + colorText);
                playerColorLabel.setForeground(gameInit.getPlayerColor() == PieceColor.WHITE ? Color.WHITE : Color.BLACK);
                playerColorLabel.setOpaque(true);
                playerColorLabel.setBackground(gameInit.getPlayerColor() == PieceColor.WHITE ? Color.BLACK : Color.LIGHT_GRAY);
            });
        });

        boardView.addMouseListener(new BoardMouseListener(new CustomMouseListenerImpl(boardView.getBoardState(), boardView, serverConnection)));
        new Thread(serverConnection).start();
    }

    private JPanel gameDataPanel(String blackName, String whiteName, int hours, int minutes, int seconds) {
        JPanel gameData = new JPanel(new GridLayout(3, 2));

        JLabel whiteLabel = new JLabel(whiteName, SwingConstants.CENTER);
        JLabel blackLabel = new JLabel(blackName, SwingConstants.CENTER);

        JLabel whiteTimeLabel = new JLabel(whiteClock.getFormattedTime(), SwingConstants.CENTER);
        JLabel blackTimeLabel = new JLabel(blackClock.getFormattedTime(), SwingConstants.CENTER);

        playerColorLabel = new JLabel("Waiting for color assignment...", SwingConstants.CENTER);
        playerColorLabel.setFont(new Font("Arial", Font.BOLD, 14));

        gameData.add(whiteLabel);
        gameData.add(blackLabel);
        gameData.add(whiteTimeLabel);
        gameData.add(blackTimeLabel);
        gameData.add(playerColorLabel);
        gameData.add(new JLabel()); // filler

        if (!(hours == 0 && minutes == 0 && seconds == 0)) {
            timer = new Timer(1000, e -> {
                Clock current = isWhiteTurn ? whiteClock : blackClock;
                JLabel currentLabel = isWhiteTurn ? whiteTimeLabel : blackTimeLabel;
                String winnerName = isWhiteTurn ? blackName : whiteName;

                current.decrementTime();
                currentLabel.setText(current.getFormattedTime());

                if (current.isTimeUp()) {
                    timer.stop();
                    int choice = JOptionPane.showConfirmDialog(gameWindow, winnerName + " wins by time! Start new game?", "Game Over", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        new StartMenu().run();
                        gameWindow.dispose();
                    } else {
                        gameWindow.dispose();
                    }
                }

                isWhiteTurn = !isWhiteTurn;
            });
            timer.start();
        } else {
            whiteTimeLabel.setText("Untimed game");
            blackTimeLabel.setText("Untimed game");
        }

        return gameData;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 0));

        JButton instr = new JButton("How to play");
        instr.addActionListener(e -> JOptionPane.showMessageDialog(gameWindow, "This is a board view. Moves are enabled.\n" + "The clock shows turns between players.", "Instructions", JOptionPane.INFORMATION_MESSAGE));

        JButton newGame = new JButton("New game");
        newGame.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(gameWindow, "Start a new game?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new StartMenu().run();
                gameWindow.dispose();
            }
        });

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(gameWindow, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (timer != null) timer.stop();
                gameWindow.dispose();
            }
        });

        buttons.add(instr);
        buttons.add(newGame);
        buttons.add(quit);
        return buttons;
    }

    private void setPiecesToTheSquares(List<PieceState> pieces, SquareState[][] squareStates) {
        Map<SquareState, PieceState> map = new HashMap<>();
        for (PieceState ps : pieces) {
            map.put(ps.getCurrentSquare(), ps);
        }

        for (int i = 0; i < squareStates.length; i++) {
            for (int j = 0; j < squareStates[i].length; j++) {
                squareStates[i][j].setOccupyingPiece(map.getOrDefault(squareStates[i][j], null));
            }
        }
    }

    public void checkmateOccurred(PieceColor pieceColor) {
        String outputMessage;
        String title;
        if (pieceColor.equals(PieceColor.BLACK)) {
            outputMessage = "White wins by checkmate! \n" + "Choosing \"OK\" lets you look at the final situation.";
            title = "White wins by checkmate!";
        } else {
            outputMessage = "Black wins by checkmate! \n" + "Choosing \"OK\" lets you look at the final situation.";
            title = "Black wins by checkmate!";
        }

        endLogicHelper(outputMessage, title);
    }

    public void stalemateOccurred() {
        String outputMessage = "Stalemate! Set up a new game? \n" + "Choosing \"No\" lets you look at the final situation.";

        String title = "Draw!";
        endLogicHelper(outputMessage, title);
    }

    private void endLogicHelper(String outputMessage, String title) {
        if (timer != null) timer.stop();

        // Custom buttons
        Object[] options = {"OK", "Export Game"};

        int choice = JOptionPane.showOptionDialog(
                gameWindow,
                outputMessage,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0] // Default button
        );

        if (choice == 1) {
            // "Export Game" button clicked
            exportGame();
        }

        // After user clicks any option, go to start menu and close window
        SwingUtilities.invokeLater(new StartMenu());
        gameWindow.dispose();
    }

    private void exportGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Game");

        // Suggest .pgn file extension
        fileChooser.setSelectedFile(new File("game.pgn"));

        int userSelection = fileChooser.showSaveDialog(gameWindow);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure file has .pgn extension
            if (!fileToSave.getName().toLowerCase().endsWith(".pgn")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pgn");
            }

            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                PgnContent content = serverConnection.GetPgnContent(); // use camelCase
                if (content != null) {
                    writer.println("[White \"" + content.getUser1() + "\"]");
                    writer.println("[Black \"" + content.getUser2() + "\"]");
                    writer.println(); // Empty line between header and moves
                    writer.println(content.getGameContent().trim());
                    writer.print(" " + content.getResult());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(gameWindow, "Failed to export game.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




}
