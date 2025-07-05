package server.services.board;

import server.model.pieces.Rook;
import server.services.strategy.common.PieceInterface;
import server.services.utils.MiscellaneousUtils;
import shared.enums.PieceColor;

import java.util.List;

public class KingMove extends MoveDecorator {

    public KingMove(Move move) {
        super(move);
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        List<PieceInterface> pieces = currentPiece.getPieceColor() == PieceColor.WHITE
                ? boardService.getWhitePieces()
                : boardService.getBlackPieces();

        final int[] distance = new int[1];

        PieceInterface rook = pieces.stream().filter(piece -> {
            if (!(piece instanceof Rook rook1) || rook1.isWasMoved() || currentPiece.isWasMoved()) {
                return false;
            }

            int rookX = rook1.getCurrentSquare().getXNum();
            int kingX = targetSquare.getXNum();
            distance[0] = Math.abs(rookX - kingX);

            return distance[0] == 1 || distance[0] == 2;
        }).findFirst().orElse(null);


        boolean rookTargetSquareUnderThreat;
        boolean kingTargetSquareUnderThreat = MiscellaneousUtils.isSquareUnderThreat(boardService, targetSquare);
        boolean kingInThreat = MiscellaneousUtils.isSquareUnderThreat(boardService, currentPiece.getCurrentSquare());
        boolean isCastlingMove = false;

        SquareInterface[][] squareArray = boardService.getBoardSquareArray();
        SquareInterface rookTargetSquare = null;

        if (rook == null) {
            return super.makeMove(currentPiece, targetSquare, boardService);
        }

        //Rook on right hand side
        if (distance[0] == 1) {

            rookTargetSquare = squareArray[targetSquare.getYNum()][5];
            rookTargetSquareUnderThreat = MiscellaneousUtils.isSquareUnderThreat(boardService, rookTargetSquare);

            if (!kingInThreat && !kingTargetSquareUnderThreat && !rookTargetSquareUnderThreat) {
                isCastlingMove = true;
            }

        } else if (distance[0] == 2) {

            rookTargetSquare = squareArray[targetSquare.getYNum()][3];
            rookTargetSquareUnderThreat = MiscellaneousUtils.isSquareUnderThreat(boardService, rookTargetSquare);

            boolean isKnightSquareOccupied = squareArray[targetSquare.getYNum()][1].isOccupied();

            if (!isKnightSquareOccupied && !kingInThreat && !kingTargetSquareUnderThreat && !rookTargetSquareUnderThreat) {
                isCastlingMove = true;
            }
        }

        return isCastlingMove ?
                super.makeMove(currentPiece, targetSquare, boardService)
                        && super.makeMove(rook, rookTargetSquare, boardService)
                : super.makeMove(currentPiece, targetSquare, boardService);
    }

}
