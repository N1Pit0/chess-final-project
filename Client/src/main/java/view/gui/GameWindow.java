package view.gui;

import client.ServerConnection;
import dtos.BoardState;
import dtos.PieceState;
import dtos.SquareState;
import model.board.Board;
import services.board.BoardInterface;
import services.utils.Clock;
import view.BoardStateView;
import view.mouseListener.BoardMouseListener;
import view.mouseListener.CustomMouseListenerImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static enums.ImagePath.RESOURCES_WPAWN_PNG;

public class GameWindow {
    private final JFrame gameWindow;
    private ServerConnection serverConnection;
    private BoardStateView boardView;
    private Clock blackClock;
    private Clock whiteClock;
    private Timer timer;
    private boolean isWhiteTurn = true;

    public GameWindow(String blackName, String whiteName, int hh, int mm, int ss) {
        blackClock = new Clock(hh, ss, mm);
        whiteClock = new Clock(hh, ss, mm);

        gameWindow = new JFrame("Chess");

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
        });

        boardView.addMouseListener(new BoardMouseListener(new CustomMouseListenerImpl(boardView.getBoardState(), boardView, serverConnection)));
        new Thread(serverConnection).start();
    }

    private JPanel gameDataPanel(String blackName, String whiteName, int hours, int minutes, int seconds) {
        JPanel gameData = new JPanel(new GridLayout(2, 2));

        JLabel whiteLabel = new JLabel(whiteName, SwingConstants.CENTER);
        JLabel blackLabel = new JLabel(blackName, SwingConstants.CENTER);

        JLabel whiteTimeLabel = new JLabel(whiteClock.getFormattedTime(), SwingConstants.CENTER);
        JLabel blackTimeLabel = new JLabel(blackClock.getFormattedTime(), SwingConstants.CENTER);

        gameData.add(whiteLabel);
        gameData.add(blackLabel);
        gameData.add(whiteTimeLabel);
        gameData.add(blackTimeLabel);

        if (!(hours == 0 && minutes == 0 && seconds == 0)) {
            timer = new Timer(1000, e -> {
                Clock current = isWhiteTurn ? whiteClock : blackClock;
                JLabel currentLabel = isWhiteTurn ? whiteTimeLabel : blackTimeLabel;
                String winnerName = isWhiteTurn ? blackName : whiteName;

                current.decrementTime();
                currentLabel.setText(current.getFormattedTime());

                if (current.isTimeUp()) {
                    timer.stop();
                    int choice = JOptionPane.showConfirmDialog(
                            gameWindow,
                            winnerName + " wins by time! Start new game?",
                            "Game Over",
                            JOptionPane.YES_NO_OPTION);

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
        instr.addActionListener(e -> JOptionPane.showMessageDialog(gameWindow,
                "This is a board view. Moves are enabled.\n" +
                        "The clock shows turns between players.",
                "Instructions", JOptionPane.INFORMATION_MESSAGE));

        JButton newGame = new JButton("New game");
        newGame.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(gameWindow,
                    "Start a new game?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new StartMenu().run();
                gameWindow.dispose();
            }
        });

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(gameWindow,
                    "Are you sure you want to quit?",
                    "Exit", JOptionPane.YES_NO_OPTION);
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
}
