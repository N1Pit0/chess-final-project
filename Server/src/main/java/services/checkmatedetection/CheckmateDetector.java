package services.checkmatedetection;

import services.board.BoardService;
import services.enums.PieceColor;

public interface CheckmateDetector {
    boolean isInCheck(BoardService boardService, PieceColor pieceColor);

    boolean isInCheckmate(BoardService boardService, PieceColor pieceColor);

    boolean isInStalemate(BoardService boardService, PieceColor pieceColor);
}
