package services.checkmatedetection;

import dtos.enums.PieceColor;
import services.board.BoardService;

public interface CheckmateDetector {
    boolean isInCheck(BoardService boardService, PieceColor pieceColor);

    boolean isInCheckmate(BoardService boardService, PieceColor pieceColor);

    boolean isInStalemate(BoardService boardService, PieceColor pieceColor);
}
