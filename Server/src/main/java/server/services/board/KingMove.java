package server.services.board;

import server.model.pieces.Rook;
import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

import java.util.List;

public class KingMove extends MoveDecorator{

    public KingMove(Move move) {
        super(move);
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        List<PieceInterface> pieces = currentPiece.getPieceColor() == PieceColor.WHITE
                ? boardService.getWhitePieces()
                : boardService.getBlackPieces();

        PieceInterface rook = pieces.stream().filter(piece -> {
            if (!(piece instanceof Rook rook1) || rook1.isWasMoved() || currentPiece.isWasMoved()) {
                return false;
            }

            int rookX = rook1.getCurrentSquare().getXNum();
            int kingX = targetSquare.getXNum();
            int distance = Math.abs(rookX - kingX);

            return distance == 1 || distance == 2;
        }).findFirst().orElse(null);

        //If rook is not null it means that there is a path between the rook and the king and king is able to make castling
        //But make sure to check if targetSquare and one which rook will end up is not under threat
        //One way to do it is like we are doing it in the CheckmateDetector

        //TODO Check if squares on which rook and king will eventually be on is under threat!

        return super.makeMove(currentPiece, targetSquare, boardService);
    }

}
