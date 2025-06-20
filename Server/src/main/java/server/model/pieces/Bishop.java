package server.model.pieces;

import shared.enums.PieceColor;
import shared.enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;
import server.services.strategy.BishopStrategy;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.BISHOP);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new BishopStrategy(this).getLegalMoves(squareArrayBoard);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "B";
    }

}
