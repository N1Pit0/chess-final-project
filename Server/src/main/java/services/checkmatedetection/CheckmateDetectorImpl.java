package services.checkmatedetection;

import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static services.enums.PieceColor.WHITE;

public class CheckmateDetectorImpl implements CheckmateDetector {

    @Override
    public boolean isInCheck(BoardService boardService, PieceColor pieceColor) {

        return pieceColor.equals(WHITE)
                ? checkHelper(boardService, PieceColor.WHITE)
                : checkHelper(boardService, PieceColor.BLACK);
    }

    @Override
    public boolean isInCheckmate(BoardService boardService, PieceColor pieceColor) {
        if (!isInCheck(boardService, pieceColor)) {
            return false;
        }

        return !hasLegalMoveWithoutCheck(boardService, pieceColor);
    }

    @Override
    public boolean isInStalemate(BoardService boardService, PieceColor pieceColor) {
        if (isInCheck(boardService, pieceColor)) {
            return false;
        }

        return !hasLegalMoveWithoutCheck(boardService, pieceColor);
    }

    private boolean hasLegalMoveWithoutCheck(BoardService boardService, PieceColor pieceColor) {
        List<PieceInterface> currentPlayerPieces = pieceColor.equals(WHITE) ? boardService.getWhitePieces() : boardService.getBlackPieces();

        return currentPlayerPieces.stream()
                .flatMap(piece -> piece.getLegalMoves(boardService.getBoardSquareArray()).stream().map(targetSquare -> {
                    SquareInterface originalSquare = piece.getCurrentSquare();
                    PieceInterface capturedPiece = targetSquare.getOccupyingPiece();

                    // Make the move
                    piece.move(targetSquare, boardService);

                    // Check if the king is in check after the move
                    boolean isInCheckAfterMove = isInCheck(boardService, pieceColor);

                    // Undo the move
                    piece.move(originalSquare, boardService);
                    if (capturedPiece != null) {
                        targetSquare.setOccupyingPiece(capturedPiece);
                        if (capturedPiece.getPieceColor() == WHITE) {
                            List<PieceInterface> updatedWhitePieces = new ArrayList<>(boardService.getWhitePieces());
                            updatedWhitePieces.add(capturedPiece);
                            boardService.setWhitePieces(updatedWhitePieces);
                        } else {
                            List<PieceInterface> updatedBlackPieces = new ArrayList<>(boardService.getBlackPieces());
                            updatedBlackPieces.add(capturedPiece);
                            boardService.setBlackPieces(updatedBlackPieces);
                        }
                    }

                    return !isInCheckAfterMove;
                }))
                .anyMatch(canGetOutOfCheck -> canGetOutOfCheck);
    }

    private boolean checkHelper(BoardService boardService, PieceColor pieceColor) {
        Optional<PieceInterface> optionalKing = pieceColor.equals(PieceColor.WHITE) ? boardService.getWhiteKing() : boardService.getBlackKing();

        if (optionalKing.isEmpty()) {
            return false;
        }

        PieceInterface king = optionalKing.get();

        List<PieceInterface> opponentPieces = king
                .getPieceColor()
                .equals(PieceColor.WHITE) ? boardService.getBlackPieces() : boardService.getWhitePieces();

        return opponentPieces.stream().anyMatch(piece ->
                piece.getLegalMoves(boardService.getBoardSquareArray()).contains(king.getCurrentSquare()));
    }
}
