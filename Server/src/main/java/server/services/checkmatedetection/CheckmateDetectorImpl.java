package server.services.checkmatedetection;

import org.springframework.stereotype.Service;
import server.services.board.BoardService;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

import java.util.List;
import java.util.Optional;

@Service
public class CheckmateDetectorImpl implements CheckmateDetector {

    @Override
    public boolean isInCheck(BoardService boardService, PieceColor pieceColor) {

        return pieceColor.equals(PieceColor.WHITE)
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
        List<PieceInterface> currentPlayerPieces = pieceColor.equals(PieceColor.WHITE) ? boardService.getWhitePieces() : boardService.getBlackPieces();

        int[][] marked = new int[8][8];

        return currentPlayerPieces.stream()
                .flatMap(piece -> piece.getLegalMoves(boardService.getBoardSquareArray()).stream().map(targetSquare -> {
                    int yNum = targetSquare.getYNum(), xNum = targetSquare.getXNum();

                    if (marked[yNum][xNum] == 1) {
                        return false;
                    }

                    SquareInterface originalSquare = piece.getCurrentSquare();
                    PieceInterface capturedPiece = targetSquare.getOccupyingPiece();

                    // Make the move
                    piece.movePiece(targetSquare, boardService);

                    // Check if the king is in check after the move
                    boolean isInCheckAfterMove = isInCheck(boardService, pieceColor);

                    // Undo the move
                    piece.movePiece(originalSquare, boardService);
                    if (capturedPiece != null) {
                        targetSquare.setOccupyingPiece(capturedPiece);
                        if (capturedPiece.getPieceColor() == PieceColor.WHITE) {
                            boardService.getWhitePieces().add(capturedPiece);
                        } else {
                            boardService.getBlackPieces().add(capturedPiece);
                            ;
                        }
                    }

                    if (isInCheckAfterMove) {
                        marked[yNum][xNum] = 1;
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
