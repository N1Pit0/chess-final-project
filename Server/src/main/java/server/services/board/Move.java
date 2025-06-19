package server.services.board;

import shared.enums.PieceColor;
import server.services.strategy.common.PieceInterface;

public interface Move {

    static boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        PieceInterface occupyingPiece = targetSquare.getOccupyingPiece();

        if (occupyingPiece != null) {
            if (occupyingPiece.getPieceColor() == currentPiece.getPieceColor()) return false;
            else capture(currentPiece, targetSquare, boardService);
        }

        removePiece(currentPiece.getCurrentSquare());
        currentPiece.setCurrentSquare(targetSquare);
        currentPiece.getCurrentSquare().put(currentPiece);
        return true;
    }

    private static void removePiece(SquareInterface targetSquare) {
        targetSquare.setOccupyingPiece(null);
    }

    private static void capture(PieceInterface piece, SquareInterface targetSquare, BoardService boardService) {

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
