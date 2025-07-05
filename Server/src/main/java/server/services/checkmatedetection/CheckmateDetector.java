package server.services.checkmatedetection;

import server.services.board.BoardService;
import shared.enums.PieceColor;

public interface CheckmateDetector {
    boolean isInCheck(BoardService boardService, PieceColor pieceColor);

    boolean isInCheckmate(BoardService boardService, PieceColor pieceColor);

    boolean isInStalemate(BoardService boardService, PieceColor pieceColor);
}
