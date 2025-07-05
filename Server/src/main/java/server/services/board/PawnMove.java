package server.services.board;

import lombok.Setter;
import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

import java.util.List;

public class PawnMove extends MoveDecorator {

    @Setter
    private PieceFactoryInterface pieceFactory;

    public PawnMove(Move move) {
        super(move);
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {

        boolean result = super.makeMove(currentPiece, targetSquare, boardService);

//        if (result) {
//            promote(currentPiece, targetSquare, boardService);
//        }

        return result;
    }

    private boolean isPromotion(int y){
        return y == 0 || y == 7;
    }

    private void promote(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        int y = targetSquare.getYNum();

        if(y % 8 != 0 && y % 8 != 7) return;

        PieceColor pieceColor = currentPiece.getPieceColor();

        List<PieceInterface> pieces = currentPiece.getPieceColor().equals(PieceColor.WHITE)
                ?boardService.getWhitePieces()
                : boardService.getBlackPieces();

        PieceInterface newPiece = pieceFactory.getQueen(pieceColor, targetSquare);

        pieces.remove(currentPiece);
        pieces.add(newPiece);

        targetSquare.setOccupyingPiece(newPiece);
        newPiece.setCurrentSquare(targetSquare);
        newPiece.setWasMoved(true);
    }
}
