import dtos.BoardState;
import dtos.PieceState;
import dtos.SquareState;
import model.board.Board;
import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;
import view.BoardStateView;
import view.BoardView;
import view.gui.NewStartMenu;

import javax.swing.*;
import java.util.*;

public class BoardRender implements Runnable {

    public static void main(String[] args) {
//         Ensure GUI runs on the Event Dispatch Thread
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Chess Game");
//
//
//            BoardInterface board = new Board();
//            // Replace with actual BoardService implementation
////            BoardService boardService = new BoardServiceImpl(board); // You must initialize this properly
//            BoardState boardState = board.toBoardState();
//            List<PieceState> pieceStates = new LinkedList<>();
//            pieceStates.addAll(boardState.getWhitePieces());
//            pieceStates.addAll(boardState.getBlackPieces());
//            setPiecesToTheSquares(pieceStates,boardState.getBoardSquareArray());
//            BoardStateView boardView = new BoardStateView(boardState);
//
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add(boardView);
//            frame.pack(); // Sizes the frame so that all contents are at or above their preferred sizes
//            frame.setLocationRelativeTo(null); // Centers the window
//            frame.setVisible(true);
//        });
        SwingUtilities.invokeLater(new NewStartMenu());
    }

    private static void setPiecesToTheSquares(List<PieceState> pieces, SquareState[][] squareStates){
        Map<SquareState,PieceState> squareToPiece = new HashMap<>();
        for (PieceState pieceState : pieces) {
            squareToPiece.put(pieceState.getCurrentSquare(), pieceState);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squareStates[i][j].setOccupyingPiece(squareToPiece.getOrDefault(squareStates[i][j],null));
            }
        }
    }


    @Override
    public void run() {

    }
}
