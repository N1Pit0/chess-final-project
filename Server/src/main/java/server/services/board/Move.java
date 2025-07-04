package server.services.board;

import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

public interface Move {
    boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService);

    default void removePiece(SquareInterface targetSquare) {
        targetSquare.setOccupyingPiece(null);
    }

    default void capture(PieceInterface piece, SquareInterface targetSquare, BoardService boardService) {

        PieceInterface targetPiece = targetSquare.getOccupyingPiece();
        PieceColor targetPieceColor = targetPiece.getPieceColor();

        if (targetPieceColor.equals(PieceColor.BLACK)) {
            boardService.getBlackPieces().remove(targetPiece);
        }
        if (targetPieceColor.equals(PieceColor.WHITE)) {
            boardService.getWhitePieces().remove(targetPiece);
        }

        targetSquare.setOccupyingPiece(piece);
    }
}
