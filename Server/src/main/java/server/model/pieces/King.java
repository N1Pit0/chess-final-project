package server.model.pieces;

import shared.enums.PieceColor;
import shared.enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;

import server.services.strategy.KingStrategy;

import java.util.List;

public class King extends Piece {

    public King(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.KING);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return new KingStrategy(this).getLegalMoves(squareArrayBoard);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "K";
    }

}
