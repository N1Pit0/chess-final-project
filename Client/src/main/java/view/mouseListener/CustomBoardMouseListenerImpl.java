package view.mouseListener;

import controller.GameController;
import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;
import view.BoardView;
import view.SquareView;
import view.gui.GameWindow;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CustomBoardMouseListenerImpl implements CustomBoardMouseListener {

    private final BoardView boardView;
    private final BoardMouseListener boardMouseListener;
    private final BoardMouseMotionListener boardMouseMotionListener;
    private final GameWindow gameWindow;
    private final GameController gameController;

    public CustomBoardMouseListenerImpl(BoardView boardView, GameWindow gameWindow, GameController gameController) {
        this.gameWindow = gameWindow;
        this.boardView = boardView;
        this.gameController = gameController;
        this.boardMouseListener = new BoardMouseListener(this);
        this.boardMouseMotionListener = new BoardMouseMotionListener(this);
        this.boardView.addMouseListener(boardMouseListener);
        this.boardView.addMouseMotionListener(boardMouseMotionListener);
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        BoardService boardService = boardView.getBoardService();

        boardService.setCurrX(e.getX());
        boardService.setCurrY(e.getY());

        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        SquareInterface targetSquare = squareView.getSquare();

        if (gameController.handlePress(targetSquare)) {
            squareView.setDisplayPiece(true);
            boardView.repaint();
        }

    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        SquareView squareView = (SquareView) boardView.getComponentAt(new Point(e.getX(), e.getY()));
        BoardService boardService = boardView.getBoardService();

        SquareInterface targetSquare = squareView.getSquare();

        if (gameController.handleRelease(targetSquare)) {
            boardService.setCurrPiece(null);
            boardView.repaint();
        }

    }

    @Override
    public void handleMouseDragged(MouseEvent e) {
        boardView.getBoardService().setCurrX(e.getX());
        boardView.getBoardService().setCurrY(e.getY());
        boardView.repaint();
    }

    private void setupBoardForCheckmate(BoardService boardService, PieceColor pieceColor) {
        boardService.setCurrPiece(null);
        boardView.repaint();
        boardView.removeMouseListener(boardMouseListener);
        boardView.removeMouseMotionListener(boardMouseMotionListener);
        gameWindow.checkmateOccurred(pieceColor);
    }

}
