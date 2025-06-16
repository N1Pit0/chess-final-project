import model.board.Board;
import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;
import view.BoardView;

import javax.swing.*;

public class BoardRender {

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Game");


            BoardInterface board = new Board();
            // Replace with actual BoardService implementation
            BoardService boardService = new BoardServiceImpl(board); // You must initialize this properly

            BoardView boardView = new BoardView(boardService);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(boardView);
            frame.pack(); // Sizes the frame so that all contents are at or above their preferred sizes
            frame.setLocationRelativeTo(null); // Centers the window
            frame.setVisible(true);
        });
    }
}
