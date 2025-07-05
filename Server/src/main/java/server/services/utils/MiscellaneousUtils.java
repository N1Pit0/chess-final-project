package server.services.utils;

import server.services.board.BoardService;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

import java.util.List;

public class MiscellaneousUtils {

    public static boolean isSquareUnderThreat(BoardService boardService, SquareInterface square) {
        PieceInterface currentPiece = boardService.getCurrPiece();
        List<PieceInterface> opponentPieces = currentPiece.getPieceColor() == PieceColor.WHITE
                ? boardService.getBlackPieces()
                : boardService.getWhitePieces();
        SquareInterface[][] squareArray = boardService.getBoardSquareArray();

        return opponentPieces.stream().anyMatch(piece ->
                piece.getLegalMoves(squareArray).contains(currentPiece.getCurrentSquare())
        );
    }

}
