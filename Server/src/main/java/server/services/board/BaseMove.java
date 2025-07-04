package server.services.board;

import server.services.strategy.common.PieceInterface;

public class BaseMove implements Move {

    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        PieceInterface occupyingPiece = targetSquare.getOccupyingPiece();

        if (occupyingPiece != null) {
            capture(currentPiece, targetSquare, boardService);
        }

        removePiece(currentPiece.getCurrentSquare());
        currentPiece.setCurrentSquare(targetSquare);
        currentPiece.getCurrentSquare().put(currentPiece);
        return true;
    }
}
