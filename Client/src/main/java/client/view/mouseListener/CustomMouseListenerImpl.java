package client.view.mouseListener;

import client.MoveSender;
import dtos.BoardState;

import client.view.BoardStateView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class CustomMouseListenerImpl implements CustomBoardMouseListener {

    private final BoardState boardState;
    private final BoardStateView boardView;
    private final MoveSender moveSender;

    private Point from = null;

    public CustomMouseListenerImpl(BoardState boardState, BoardStateView boardView, MoveSender moveSender) {
        this.boardState = boardState;
        this.boardView = boardView;
        this.moveSender = moveSender;
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        from = getBoardCoordinate(e.getX(), e.getY());
        if (from != null) {
            boardState.setCurrPiece(boardState.getBoardSquareArray()[from.y][from.x].getOccupyingPiece());
            boardState.setCurrX(e.getX());
            boardState.setCurrY(e.getY());
            boardView.repaint();
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent e) {
        if (from == null) return;

        Point to = getBoardCoordinate(e.getX(), e.getY());
        if (to == null || from.equals(to)) {
            from = null;
            boardState.setCurrPiece(null);
            boardView.repaint();
            return;
        }

        String moveStr = toAlgebraic(from) + " " + toAlgebraic(to);
        try {
            moveSender.sendMove(moveStr);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Optionally: notify user about sending failure
        }

        from = null;
        boardState.setCurrPiece(null);
        boardView.repaint();
    }

    @Override
    public void handleMouseDragged(MouseEvent e) {
        boardState.setCurrX(e.getX());
        boardState.setCurrY(e.getY());
        boardView.repaint();
    }

    private Point getBoardCoordinate(int xPixel, int yPixel) {
        int squareSize = 50;  // Assuming square size of 50 pixels
        int col = xPixel / squareSize;
        int row = yPixel / squareSize;

        if (col >= 0 && col < 8 && row >= 0 && row < 8) {
            return new Point(col, row);
        }
        return null;
    }

    private String toAlgebraic(Point p) {
        char file = (char) ('a' + p.x);
        char rank = (char) ('8' - p.y);
        return "" + file + rank;
    }
}
